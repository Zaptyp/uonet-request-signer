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
import java.util.*

class GeneratorTest {

    @Test
    fun generatorTest() {
        val (certificate, fingerprint, privateKey) = generateKeyPair()

        val certificateFactory = CertificateFactory.getInstance("X.509")
        val x509 = certificateFactory.generateCertificate(
                ByteArrayInputStream(Base64.getDecoder().decode(certificate))
        )

        val keyFactory = KeyFactory.getInstance("RSA")
        val pkcs8KeySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
        val private = keyFactory.generatePrivate(pkcs8KeySpec) as RSAPrivateCrtKey
        val rsaKeySpec = RSAPublicKeySpec(private.modulus, private.publicExponent, private.params)
        val publicKey = keyFactory.generatePublic(rsaKeySpec)

        val digest = MessageDigest.getInstance("SHA-1")
        digest.update(x509.encoded)

        assertEquals(fingerprint.length, 40)
        assertEquals(digest.digest().joinToString("") { "%02x".format(it) }, fingerprint)
        assertEquals(x509.publicKey, publicKey)
    }
}
