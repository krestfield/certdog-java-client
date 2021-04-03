# Certdog Java Client


A java client for certdog allowing you to obtain certificates as **.cer**, **PKCS#12/PFX**, **PEM/PKCS#8** or **JKS** formats  

Certdog can interface to your existing **Microsoft CAs**, **PrimeKey EJBCAs** as well as its own **internal CAs**  



More info on certdog: <https://krestfield.com/certdog>

More info on this client: <https://krestfield.github.io/docs/certdog/java_client.html>  



### Usage

Create the client, passing the API URL

```java
CertdogClient client = new CertdogClient("https://certdog.net/certdog/api");
```



Login with the api username and password

```java
client.login("certdogtest", "password");
```



To request a certificate from a CSR provide

1. The name of the cert issuer (as configured in certdog - use ``clien.getIssuers()`` to get all available issuers)
2. The name of the team you wish this certificate to be associated with
3. The CSR

```java
String certIssuer = "Certdog TLS"
String team = "Test Team"
String csr = "-----BEGIN CERTIFICATE REQUEST-----MIICVDC...IcfbvBX-----END CERTIFICATE REQUEST-----";

X509Certificate cert = client.requestCertFromCsr(certIssuer, team, csr);
```



Save

```java
CertdogClient.SaveCert(cert, "C:/temp/certdog.cer")
```



Request a cert from a DN. As well as the cert issuer and team, you also need to provide:

1. The CSR generator name. This is the generator that will create the CSR for you (use ``client.getGenerators()`` to get all available generators)
2. The requested DN
3. Any Subject Alternative Names
4. The password to protect the returned P12, JKS, PEM

```java
String csrGenerator = "RSA2048"    
String dn = "CN=domain.com,O=Some org"

// Add Subject Alternative Names in this format:
// DNS:[dnsname.com],IP:[IP Address],EMAIL:[Email Address]
// pass null for no SANs    
ArrayList<String> sans = new ArrayList<>();
sans.add("DNS:domain.com");
sans.add("DNS:domain2.com");

String p12Password = "password"
   
// Request the cert. Other response formats are: JKS and PEM (PKCS#8)
String p12Data = client.requestCert(certIssuer, csrGenerator, team,
                     dn, p12Password, sans, ResponseFormat.PKCS12);
```



Save the returned data

```java
CertdogClient.SaveP12(p12Data, "C:/temp/certdog.pfx");
```

You can now import this into your local certificate store




Revoke a certificate

```java
client.revokeCert(certIssuer, cert, RevocationReason.CesationOfOperation);
```



To find out what issuers, teams and CSR generators are available

```java
List<String> teamNames = client.getTeams();

List<String> issuers = client.getIssuers();

List<String> generators = client.getGenerators();
```



Logout

```java
client.logout();
```



Jump on the UI to view your certs

<https://certdog.net/>



