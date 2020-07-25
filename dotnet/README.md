# Uonet+ request signer for .NET

[![nuget](https://img.shields.io/nuget/v/UonetRequestSigner?style=flat-square)](https://www.nuget.org/packages/UonetRequestSigner/)

## Instalation

```bash
$ dotnet add package UonetRequestSigner
```

## Usage

```cs
using Wulkanowy

var signed = UonetRequestSigner.SignContent(Password, Certificate, ontent)
```

## Tests

```bash
$ dotnet test
```
