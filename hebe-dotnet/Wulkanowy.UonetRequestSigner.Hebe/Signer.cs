using System;
using System.Text;
using System.Linq;
using System.Text.RegularExpressions;
using System.Security.Cryptography;
using System.Collections.Generic;
using System.Web;
using Wulkanowy.UonetRequestSigner.Hebe.Exceptions;

namespace Wulkanowy.UonetRequestSigner.Hebe
{
    public static class Signer
    {
        public static (string digest, string canonicalUrl, string signature) GetSignatureValues(string fingerprint, string privateKey, 
            string body, string requestPath, DateTime timestamp)
        {
            var digest = GetDigest(body);
            var formattedTimestamp = timestamp.ToString("ddd, dd MMM yyyy hh:mm:ss 'GMT'");
            var headers = GetHeaders(requestPath, formattedTimestamp, digest);
            
            var headersName = from header in headers select header.Item1;
            var headersValue = from header in headers select header.Item2;

            return 
            (
                $"SHA-256={digest}",
                headers[0].Item2,
                $"keyId=\"{fingerprint}\",headers=\"{string.Join(" ", headersName.ToArray())}\",algorithm=\"sha256withrsa\"," +
                $"signature=Base64(SHA256withRSA({GetSignatureValues(string.Join("", headersValue.ToArray()), privateKey)}))"
            );
        }

        private static List<(string, string)> GetHeaders(string url, string date, string digest)
        {
            var headers = new List<(string header, string value)>
            {
                ("vCanonicalUrl", GetEncodedPath(url)),
                ("Digest", digest),
                ("vDate", date)
            };

            return headers;
        }

        private static string GetEncodedPath(string path)
        {
            var rx = Regex.Match(path, "(api/mobile/.+)");
            if (!rx.Success)
                throw new NotMatchedUrlException(path);
            
            return HttpUtility.UrlEncode(rx.Value);
        }

        private static string GetDigest(string body)
        {
            var sha = SHA256.Create();
            var data = sha.ComputeHash(Encoding.UTF8.GetBytes(body));
            
            return Convert.ToBase64String(data);
        }
        
        private static string GetSignatureValues(string values, string privateKey)
        {
            var blk = Convert.FromBase64String(privateKey);
            var provider = new RSACryptoServiceProvider();
            provider.ImportPkcs8PrivateKey(new ReadOnlySpan<byte>(blk), out _);
            var signedValues = provider.SignData(Encoding.UTF8.GetBytes(values), SHA256.Create());
            
            return Convert.ToBase64String(signedValues);
        }
    }
}
