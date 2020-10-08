from uonet_request_signer_hebe import generate_key_pair
from OpenSSL import crypto
import base64


def test_generator():
    certificate, fingerprint, private_key = generate_key_pair()

    x509 = crypto.load_certificate(crypto.FILETYPE_ASN1, base64.b64decode(certificate))
    pkcs8 = crypto.load_privatekey(crypto.FILETYPE_ASN1, base64.b64decode(private_key))
    assert len(fingerprint) == 40
    assert x509.digest("sha1").decode("utf-8").replace(":", "").lower() == fingerprint
    assert crypto.dump_publickey(
        crypto.FILETYPE_PEM, x509.get_pubkey()
    ) == crypto.dump_publickey(crypto.FILETYPE_PEM, pkcs8)
