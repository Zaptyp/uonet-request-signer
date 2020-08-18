# UONET+ hebe request signer for Dart.

[![pub](https://img.shields.io/pub/v/uonet_request_signer.svg?style=flat-square)](https://pub.dev/packages/uonet_request_signer)

## Instalation

```yaml
dependencies:
  uonet_request_signer: ^1.0.0
```

## Usage

```dart
import 'package:uonet_request_signer/uonet_request_signer.dart';

void main() {
  getSignatureValues(fingerprint, privateKey, body, fullUrl, DateTime.now());
}
```

## Tests

```bash
$ pub run test
```
