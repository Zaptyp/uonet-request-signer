# Uonet+ hebe request signer for .NET

## Usage

```cs
using UonetRequestSignerHebe;

var signed = new Signer().GetSignatureValues("fingerprint", "private_key", "body", DateTime.Now, "url");
```

## Tests

```bash
$ dotnet restore
$ dotnet test
```
