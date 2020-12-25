# UONET+ Request Signer for JVM

[![](https://jitpack.io/v/wulkanowy/uonet-request-signer.svg)](https://jitpack.io/#wulkanowy/uonet-request-signer)

## Installation

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.wulkanowy.uonet-request-signer:jvm:master-SNAPSHOT'
}
```

## Usage

```kotlin
import io.github.wulkanowy.signer.signContent
import io.github.wulkanowy.signer.getPrivateKeyFromCert

// sign content using PFX certificate and API password
val signed = signContent(password, certificate, content)

// sign content using private key extracted from PFX
val signed = signContent(key, content)

// extract private key from PFX
// using a once generated private key is about 250x faster
// than using the PFX each time
val privateKey = getPrivateKeyFromCert(password, certificate)
```

## Tests

```bash
$ ./gradlew test
```
