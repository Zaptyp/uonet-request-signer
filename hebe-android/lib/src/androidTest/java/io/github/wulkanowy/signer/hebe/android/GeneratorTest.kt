package io.github.wulkanowy.signer.hebe.android

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.migcomponents.migbase64.Base64
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream
import java.math.BigInteger
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.interfaces.RSAKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPrivateKeySpec
import java.security.spec.RSAPublicKeySpec

@RunWith(AndroidJUnit4::class)
class GeneratorTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun generatorTest() {
        val (certificate, fingerprint, privateKey) = generateKeyPair(context, "KeyEntry")

        val certificateFactory = CertificateFactory.getInstance("X.509")
        val x509 = certificateFactory.generateCertificate(
            ByteArrayInputStream(Base64.decodeFast(certificate))
        )

        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = RSAPublicKeySpec((privateKey as RSAKey).modulus, BigInteger.valueOf(65537))
        val publicKey = keyFactory.generatePublic(publicKeySpec)

        val digest = MessageDigest.getInstance("SHA-1")
        digest.update(x509.encoded)

        Assert.assertEquals(fingerprint.length, 40)
        Assert.assertEquals(digest.digest().joinToString("") { "%02x".format(it) }, fingerprint)
        Assert.assertEquals(x509.publicKey, publicKey)
    }

    @Test
    fun getterTest() {
        val (certificate1, fingerprint1, privateKey1) = generateKeyPair(context, "KeyEntry")
        val (certificate2, fingerprint2, privateKey2) = getKeyEntry("KeyEntry")!!

        Assert.assertEquals(certificate1, certificate2)
        Assert.assertEquals(fingerprint1, fingerprint2)
        Assert.assertEquals(privateKey1, privateKey2)
    }
}
