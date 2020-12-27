# Uonet+ hebe request signer for .NET

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
