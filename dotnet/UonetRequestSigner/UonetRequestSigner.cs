using System;
using System.IO;
using System.Text;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Digests;
using Org.BouncyCastle.Crypto.Signers;
using Org.BouncyCastle.Pkcs;
using Org.BouncyCastle.Security;

namespace Wulkanowy
{
    public static class UonetRequestSigner
    {
        public static string SignContent(string password, string certificate, string content)
        {
            var key = GetPrivateKeyFromCert(password, certificate);
            return SignContent(key, content);
        }

        public static string SignContent(string privateKey, string content)
        {
            var key = PrivateKeyFactory.CreateKey(Convert.FromBase64String(privateKey));
            return SignContent(key, content);
        }

        public static string SignContent(AsymmetricKeyParameter privateKey, string content)
        {
            var rsaDigestSigner = new RsaDigestSigner(new Sha1Digest());
            rsaDigestSigner.Init(true, privateKey);
            var bytes = Encoding.UTF8.GetBytes(content);
            rsaDigestSigner.BlockUpdate(bytes, 0, bytes.Length);
            return Convert.ToBase64String(rsaDigestSigner.GenerateSignature());
        }

        public static AsymmetricKeyParameter GetPrivateKeyFromCert(string password, string certificate)
        {
            var keystore = new Pkcs12Store(new MemoryStream(Convert.FromBase64String(certificate)), password.ToCharArray());
            return keystore.GetKey("LoginCert").Key;
        }

        public static string GetPrivateKeyFromCertEncoded(string password, string certificate)
        {
            var privateKey = GetPrivateKeyFromCert(password, certificate);

            var keyFactory = PrivateKeyInfoFactory.CreatePrivateKeyInfo(privateKey);
            return Convert.ToBase64String(keyFactory.GetEncoded());
        }
    }
}
