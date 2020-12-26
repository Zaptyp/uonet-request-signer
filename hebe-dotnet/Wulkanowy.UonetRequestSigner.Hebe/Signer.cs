using System;
using System.Text;
using System.Linq;
using System.Text.RegularExpressions;
using System.Security.Cryptography;
using System.Collections.Generic;

namespace Wulkanowy.UonetRequestSigner.Hebe
{
    public class Signer
    {
        public (string digest, string cannonicalUrl, string signature) GetSignatureValues(string fingerprint, string privateKey, 
            string body, string requestPath, DateTime timestamp)
        {
            string digest = GetDigest(body);
            string formattedTimestamp = timestamp.ToString("ddd, dd MMM yyyy hh:mm:ss 'GMT'");
            var headers = GetHeaders(requestPath, formattedTimestamp, digest);
            
            var headersName = from header in headers select header.Item1;
            var headersValue = from header in headers select header.Item2;

            return 
            (
                $"SHA-256={digest}",
                GetEncodedPath(requestPath),
                $"keyId=\"{fingerprint}\",headers=\"{String.Join(" ", headersName.ToArray())}\",algorithm=\"sha256withrsa\"," +
                $"signature=Base64(SHA256withRSA({GetSignatureValues(String.Join("", headersValue.ToArray()), privateKey)}))"
            );
        }

        private List<(string, string)> GetHeaders(string url, string date, string digest)
        {
            var headers = new List<(string header, string value)>();
            headers.Add(("vCanonicalUrl", GetEncodedPath(url)));
            headers.Add(("Digest", digest));
            headers.Add(("vDate", date));
            
            return headers;
        }

        private string GetEncodedPath(string path)
        {
            var rx = Regex.Match(path, "(api/mobile/.+)");
            if (rx.Value == null)
                throw new Exception("The url is not finded!");
            
            return rx.Value.Replace("/", "%2f");
        }

        private string GetDigest(string body)
        {
            var sha = SHA256.Create();
            var data = sha.ComputeHash(Encoding.UTF8.GetBytes(body));
            
            return Convert.ToBase64String(data);
        }
        
        private string GetSignatureValues(string values, string privKey)
        {
            var blk = Convert.FromBase64String(privKey);
            var provider = new RSACryptoServiceProvider();
            provider.ImportPkcs8PrivateKey(new ReadOnlySpan<byte>(blk), out _);
            var signedValues = provider.SignData(Encoding.UTF8.GetBytes(values), SHA256.Create());
            
            return Convert.ToBase64String(signedValues);
        }
    }
}
