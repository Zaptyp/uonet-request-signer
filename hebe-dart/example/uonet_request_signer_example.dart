import 'package:uonet_request_signer/uonet_request_signer.dart';

void main() {
  getSignatureValues(fingerprint, privateKey, body, fullUrl, DateTime.now());
}
