# Uonet+ hebe request signer for .NET
[![nuget](https://img.shields.io/nuget/v/Wulkanowy.UonetRequestSigner.Hebe?style=flat-square)](https://www.nuget.org/packages/Wulkanowy.UonetRequestSigner.Hebe/)
## Usage

```cs
using Wulkanowy.UonetRequestSigner.Hebe;

var (digest, cannonicalUrl, signature) = Signer.GetSignatureValues("fingerprint", "private_key", "body", "url", DateTime.Now);
```

## Tests

```bash
$ dotnet restore
$ dotnet test
```
