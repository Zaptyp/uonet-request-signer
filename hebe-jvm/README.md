# UONET+ (hebe) Request Signer for JVM

[![](https://jitpack.io/v/wulkanowy/uonet-request-signer.svg)](https://jitpack.io/#wulkanowy/uonet-request-signer)

## Installation

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.wulkanowy.uonet-request-signer:hebe-jvm:master-SNAPSHOT'
}
```

Additionally, to use the library on Android, [Core Library Desugaring](https://developer.android.com/studio/write/java8-support) has to be enabled.

## Usage

Generate an RSA2048 key pair:
```kotlin
import io.github.wulkanowy.signer.hebe.android.generateKeyPair

val (publicPem, privatePem, publicHash) = generateKeyPair()
```

Generate a certificate:
```kotlin
import io.github.wulkanowy.signer.hebe.android.generateKeyPair

val (certificatePem, certificateHash) = generateCertificate(privatePem)
```

### Sign request content
```kotlin
import io.github.wulkanowy.signer.hebe.android.getSignatureHeaders

val headers = getSignatureHeaders(keyId, privatePem, body, fullUrl, ZonedDateTime.now())
```

The `keyId` depends on the `CertificateType`:
- for `X509` - SHA-1 of the raw certificate bytes (`certificateHash`)
- for `RSA_PEM` - MD5 of the PEM-encoded public key (`publicHash`)

Hashes are represented as hexadecimal strings, without spaces.
PEM encoding is considered as Base64 here, without wrapping or RSA headers.

## Tests

```bash
$ ./gradlew test
```
