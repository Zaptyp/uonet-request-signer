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

## Usage

Generate an RSA2048 key pair (private key and certificate):
```kotlin
import io.github.wulkanowy.signer.hebe.android.generateKeyPair

val (certificate, fingerprint, privateKey) = generateKeyPair()
```

Sign request content:
```kotlin
import io.github.wulkanowy.signer.hebe.android.getSignatureValues

val (digest, canonicalUrl, signature) = getSignatureValues(fingerprint, privateKey, body, fullUrl, Date())
```

## Tests

```bash
$ ./gradlew test
```
