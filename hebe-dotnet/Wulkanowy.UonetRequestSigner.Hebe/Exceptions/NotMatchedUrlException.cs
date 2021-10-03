using System;

namespace Wulkanowy.UonetRequestSigner.Hebe.Exceptions
{
    public class NotMatchedUrlException : Exception
    {
        public NotMatchedUrlException(string value) : base($"{value} is not matched with regex")
        {
        }
    }
}