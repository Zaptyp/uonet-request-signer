package io.github.wulkanowy.signer.hebe

import com.migcomponents.migbase64.Base64
import eu.szkolny.x509.X509Generator
import java.net.URLEncoder
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.security.MessageDigest.getInstance as createSign

private fun getDigest(body: String?): String? {
    if (body == null) return null
    return Base64.encodeToString(createSign("SHA-256").digest(body.toByteArray()), false)
}

private fun getSignatureValue(values: String, privatePem: String): String {
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateBytes = Base64.decode(privatePem)
    val privateSpec = PKCS8EncodedKeySpec(privateBytes)
    val privateKey = keyFactory.generatePrivate(privateSpec)

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(values.toByteArray())

    return Base64.encodeToString(signature.sign(), false)
}

private fun getEncodedPath(path: String): String {
    val url = ("(api/mobile/.+)".toRegex().find(path))
        ?: throw IllegalArgumentException("The URL does not seem correct (does not match `(api/mobile/.+)` regex)")

    return URLEncoder.encode(url.groupValues[0], "UTF-8").orEmpty().toLowerCase()
}

private fun getHeaders(digest: String?, canonicalUrl: String, timestamp: ZonedDateTime): MutableMap<String, String> {
    val headers = mutableMapOf<String, String>()
    headers["vCanonicalUrl"] = canonicalUrl
    if (digest != null) headers["Digest"] = digest
    headers["vDate"] = timestamp.format(DateTimeFormatter.RFC_1123_DATE_TIME)
    return headers
}

fun getSignatureHeaders(
    keyId: String,
    privatePem: String,
    body: String?,
    requestPath: String,
    timestamp: ZonedDateTime
): Map<String, String> {
    val canonicalUrl = getEncodedPath(requestPath)
    val digest = getDigest(body)
    val headers = getHeaders(digest, canonicalUrl, timestamp.withZoneSameInstant(ZoneId.of("GMT")))
    val headerNames = headers.keys.joinToString(" ")
    val headerValues = headers.values.joinToString("")
    val signatureValue = getSignatureValue(headerValues, privatePem)

    headers["Digest"] = "SHA-256=${digest}"
    headers["Signature"] = """keyId="$keyId",headers="$headerNames",algorithm="sha256withrsa",signature=Base64(SHA256withRSA($signatureValue))"""

    return headers
}

fun generateKeyPair(): Triple<String, String, String> {
    val generator = KeyPairGenerator.getInstance("RSA")
    generator.initialize(2048)
    val keyPair = generator.generateKeyPair()
    val publicKey = keyPair.public.encoded
    val privateKey = keyPair.private.encoded

    val publicPem = Base64.encodeToString(publicKey, false)
    val privatePem = Base64.encodeToString(privateKey, false)
    val publicHash = createSign("MD5")
            .digest(publicPem.toByteArray())
            .joinToString("") { "%02x".format(it) }
    return Triple(publicPem, privatePem, publicHash)
}

fun generateCertificate(privatePem: String): Pair<String, String> {
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateBytes = Base64.decode(privatePem)
    val privateSpec = PKCS8EncodedKeySpec(privateBytes)
    val privateKey = keyFactory.generatePrivate(privateSpec) as RSAPrivateCrtKey
    val publicSpec = RSAPublicKeySpec(privateKey.modulus, privateKey.publicExponent, privateKey.params)
    val publicKey = keyFactory.generatePublic(publicSpec)
    val keyPair = KeyPair(publicKey, privateKey)

    val notBefore = ZonedDateTime.now()
    val notAfter = notBefore.plusYears(20)

    val cert = X509Generator(X509Generator.Algorithm.RSA_SHA256)
            .generate(subject = mapOf("CN" to "APP_CERTIFICATE CA Certificate"),
                    notBefore = notBefore,
                    notAfter = notAfter,
                    serialNumber = 1,
                    keyPair = keyPair
            )

    val certificatePem = Base64.encodeToString(cert, false)
    val certificateHash = createSign("SHA-1")
            .digest(cert)
            .joinToString("") { "%02x".format(it) }
    return Pair(certificatePem, certificateHash)
}
