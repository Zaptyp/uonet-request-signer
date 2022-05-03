# UONET+ (hebe) Request Signer for Android

## Installation

```groovy
allprojects {
    repositories {
        mavenLocal()
    }
}

dependencies {
    implementation "io.github.wulkanowy:uonet-request-signer-hebe-android:0.1.0"
}
```

## Usage

Apart from other platforms, the privateKey returned from the generator is **not**
a PEM-encoded string; it is a PrivateKey object suitable for the Android signer only.

Generate an RSA2048 key pair (private key and certificate):
```kotlin
import io.github.wulkanowy.signer.hebe.android.generateKeyPair
import io.github.wulkanowy.signer.hebe.android.getKeyEntry

val alias = "KeyEntry" // name in the Android KeyStore

// the key entry is saved in the AndroidKeyStore and returned here
val (certificate, fingerprint, privateKey) = generateKeyPair(context, alias)

// to get the key entry for usage later:
val (certificate, fingerprint, privateKey) = getKeyEntry(alias)
```

Sign request content:
```kotlin
import io.github.wulkanowy.signer.hebe.android.getSignatureValues

// the privateKey here may be either a PEM-encoded string (like in other implementations)
// or a PrivateKey object, returned from the generator above
val (digest, canonicalUrl, signature) = getSignatureValues(fingerprint, privateKey, body, fullUrl, Date())
```

## Tests

```bash
$ ./gradlew test
```
