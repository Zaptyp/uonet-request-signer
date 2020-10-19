# UONET+ (hebe) Request Signer for Android

[![Bintray](https://img.shields.io/bintray/v/wulkanowy/wulkanowy/uonet-request-signer-hebe-android.svg?style=flat-square)](https://bintray.com/wulkanowy/wulkanowy/uonet-request-signer-hebe-android)

## Installation

```groovy
allprojects {
    repositories {
        maven { url "https://dl.bintray.com/wulkanowy/wulkanowy" }
    }
}

dependencies {
    implementation "io.github.wulkanowy:uonet-request-signer-hebe-android:0.1.0"
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
