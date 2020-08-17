import 'dart:convert';

import 'package:crypto/crypto.dart';
import 'package:encrypt/encrypt.dart';
import 'package:pointycastle/asymmetric/api.dart';
import 'package:intl/intl.dart';

class _SignatureValues {
  String digest;
  String canonicalUrl;
  String signature;
}

String _getDigest(String body) {
  if (body == null) return '';
  return base64Encode(sha256.convert(utf8.encode(body)).bytes);
}

String _getSignatureValue(String values, String pkey) {
  final parser = RSAKeyParser();
  final key = parser.parse('-----BEGIN PRIVATE KEY-----\n' +
      pkey +
      '\n-----END PRIVATE KEY-----') as RSAPrivateKey;
  final signer = Signer(RSASigner(RSASignDigest.SHA256, privateKey: key));
  return signer.sign(values).base64;
}

String _getEncodedPath(String path) {
  final url = RegExp(r'(api/mobile/.+)').stringMatch(path);
  if (url == null) {
    throw Exception(
        'The URL does not seem correct (does not match `(api/mobile/.+)` regex)');
  }
  return Uri.encodeComponent(url).toLowerCase();
}

Map<String, String> _getHeadersList(
    String body, String digest, String canonicalUrl, DateTime timestamp) {
  final signData = <String, String>{'vCanonicalUrl': canonicalUrl};
  if (body != null) signData['Digest'] = digest;
  final formatter = DateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'");
  signData['vDate'] = formatter.format(timestamp);
  return {
    'headers': signData.keys.join(' '),
    'values': signData.values.join('')
  };
}

_SignatureValues getSignatureValues(String fingerprint, String privateKey,
    String body, String requestPath, DateTime timestamp) {
  final canonicalUrl = _getEncodedPath(requestPath);
  final digest = _getDigest(body);
  final headers = _getHeadersList(body, digest, canonicalUrl, timestamp);
  final signatureValue = _getSignatureValue(headers['values'], privateKey);

  var signatureValues = _SignatureValues();
  signatureValues.digest = 'SHA-256=$digest';
  signatureValues.canonicalUrl = canonicalUrl;
  signatureValues.signature =
      'keyId="$fingerprint",headers="${headers['headers']}",algorithm="sha256withrsa",signature=Base64(SHA256withRSA($signatureValue))';
  return signatureValues;
}
