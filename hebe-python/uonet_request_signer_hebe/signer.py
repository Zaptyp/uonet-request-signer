from OpenSSL import crypto
import hashlib
import base64
import json
import re
import urllib
from datetime import datetime


def get_encoded_path(full_url):
    path = re.search(r'(api/mobile/.+)', full_url)
    if path is None:
        raise ValueError('The URL does not seem correct (does not match `(api/mobile/.+)` regex)')
    return urllib.parse.quote(path[1], safe='').lower()


def get_digest(body):
    if not body:
        return None
    
    m = hashlib.sha256()
    m.update(bytes(body, 'utf-8'))
    return base64.b64encode(m.digest()).decode('utf-8')


def get_headers_list(body, digest, canonical_url, timestamp):
    sign_data = [
        ['vCanonicalUrl', canonical_url],
        ['Digest', digest] if body else None,
        ['vDate', timestamp.strftime('%a, %d %b %Y %H:%M:%S GMT')]
    ]
    
    return (
        ' '.join(item[0] for item in sign_data if item),
        ''.join(item[1] for item in sign_data if item)
    )


def get_signature(data, private_key):
    data_str = json.dumps(data) if isinstance(data, dict) or isinstance(data, list) else str(data)
    pkcs8 = crypto.load_privatekey(crypto.FILETYPE_ASN1, base64.b64decode(private_key))
    signature = crypto.sign(pkcs8, bytes(data_str, 'utf-8'), 'RSA-SHA256')
    return base64.b64encode(signature).decode('utf-8')


def get_signature_values(fingerprint, private_key, body, full_url, timestamp):
    canonical_url = get_encoded_path(full_url)
    digest = get_digest(body)
    headers, values = get_headers_list(body, digest, canonical_url, timestamp)
    signature = get_signature(values, private_key)

    return (
        f'SHA-256={digest}' if digest else None,
        canonical_url,
        f'keyId="{fingerprint}",headers="{headers}",algorithm="sha256withrsa",signature=Base64(SHA256withRSA({signature}))'
    )
