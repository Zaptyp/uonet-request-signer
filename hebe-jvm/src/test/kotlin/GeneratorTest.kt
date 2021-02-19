package io.github.wulkanowy.signer.hebe

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

class GeneratorTest {

    @Test
    fun generatorTest() {
        val (publicPem, privatePem, publicHash) = generateKeyPair()
        val (certificatePem, certificateHash) = generateCertificate(privatePem)

        val certificateFactory = CertificateFactory.getInstance("X.509")
        val x509 = certificateFactory.generateCertificate(
                ByteArrayInputStream(Base64.getDecoder().decode(certificatePem))
        )

        val keyFactory = KeyFactory.getInstance("RSA")
        val privateBytes = Base64.getDecoder().decode(privatePem)
        val privateSpec = PKCS8EncodedKeySpec(privateBytes)
        val privateKey = keyFactory.generatePrivate(privateSpec) as RSAPrivateCrtKey
        val publicSpec = RSAPublicKeySpec(privateKey.modulus, privateKey.publicExponent, privateKey.params)
        val publicKey = keyFactory.generatePublic(publicSpec)

        val publicSpec2 = X509EncodedKeySpec(Base64.getDecoder().decode(publicPem))
        val publicKey2 = keyFactory.generatePublic(publicSpec2)

        val sha1 = MessageDigest.getInstance("SHA-1")
        val md5 = MessageDigest.getInstance("MD5")

        assertEquals(certificateHash.length, 40)
        assertEquals(sha1.digest(x509.encoded).joinToString("") { "%02x".format(it) }, certificateHash)
        assertEquals(x509.publicKey, publicKey)
        assertEquals(publicKey, publicKey2)
        x509.verify(publicKey2)
        assertEquals(x509.type, "X.509")
        assertEquals(x509.publicKey.algorithm, "RSA")
        assertEquals(md5.digest(
                Base64.getEncoder()
                        .encodeToString(publicKey.encoded)
                        .toByteArray()
        ).joinToString("") { "%02x".format(it) }, publicHash)
    }
}
