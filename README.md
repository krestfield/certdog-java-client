# Certdog Java Client


A java client for certdog allowing you to obtain certificates as **.cer**, **PKCS#12/PFX**, **PEM/PKCS#8** or **JKS** formats  

Certdog can interface to your existing **Microsoft CAs**, **PrimeKey EJBCAs** as well as its own **internal CAs**  



More info on certdog: <https://krestfield.com/certdog>

More info on this client: <https://krestfield.github.io/docs/certdog/java_client.html>  



### Usage

Create the client

```java
CertdogClient client = new CertdogClient("https://certdog.net/certdog/api");
```



Login

```java
// Login with your API username and password
client.login("certdogtest", "password");
```



Request a cert from a DN

```java
// The issuer that is configured in certdog - this may be a 
// Microsoft CA, EJBCA or Internal CA
String certIssuer = "Certdog TLS"

// When requesting via a DN, certdog generates the CSR for you
// This is the name of the configured generator    
String csrGenerator = "RSA2048"    

// The team this certificate will be associated with    
String team = "Test Team"     

// The requested DN    
String dn = "CN=domain.com,O=Some org"

// Add Subject Alternative Names in this format:
// DNS:[dnsname.com],IP[IP Address],EMAIL:[Email Address]
ArrayList<String> sans = new ArrayList<>();
sans.add("DNS:domain.com");

// The password to protect the returned P12/PFX data
String p12Password = "password"
    
// Request the cert. Other response options are: JKS and PEM (PKCS#8)
String p12Data = client.requestCert(certIssuer, csrGenerator, team,
                     dn, p12Password, sans, null, null, ResponseFormat.PKCS12);
```



Save the returned data

```java
CertdogClient.SaveP12(p12Data, "C:/temp/certdog.pfx");
```

You can now import this into your local certificate store



To request a certificate from a CSR

```java
String csr = "-----BEGIN CERTIFICATE REQUEST-----MIICVDC...IcfbvBX-----END CERTIFICATE REQUEST-----";
String certData = client.requestCertFromCsr(certIssuer, team, csr, null, null);
```



Save as a cert

```java
CertdogClient.SaveCert(certData, "C:/temp/certdog.cer")
```



Or just get the returned data as an X509Certificate

```java
X509Certificate cert = CertdogClient.GetCertFromData(certData);
```



Revoke the certificate

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



