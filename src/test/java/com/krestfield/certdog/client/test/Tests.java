package com.krestfield.certdog.client.test;

import com.krestfield.certdog.client.CertdogClient;
import com.krestfield.certdog.client.CertdogClient.RevocationReason;
import com.krestfield.certdog.client.CertdogClient.ResponseFormat;
import org.junit.jupiter.api.Test;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class Tests
{
    //public static final String APIURL = "http://127.0.0.1:8081/api";
    public static final String APIURL = "https://certdog.net/certdog/api";
    //public static final String APIUSER = "admin";
    public static final String APIUSER = "certdogtest";
    public static final String APIPASS = "password";
    public static final String SAVEFOLDER = "C:/temp/";

    @Test
    public void login() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);
        client.logout();
    }

    @Test
    public void getDetails() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams();
        System.out.println("\nAvailable Teams:");
        for (String t : teamNames){System.out.println(t);}

        List<String> issuers = client.getIssuers();
        System.out.println("\nAvailable Issuers:");
        for (String i : issuers){System.out.println(i);}

        List<String> generators = client.getGenerators();
        System.out.println("\nAvailable CSR Generators:");
        for (String g : generators){System.out.println(g);}

        client.logout();
    }

    @Test
    public void getCertP12() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams();
        List<String> issuers = client.getIssuers();
        List<String> generators = client.getGenerators();

        String p12Data = client.requestCert(issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client p12 test", "password", null, null, null, ResponseFormat.PKCS12);
        p12Data = client.requestCert(issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client p12 test", "password", null, ResponseFormat.PKCS12);
        System.out.println("\nCert issued OK. P12 Data:\n" + p12Data);


        CertdogClient.SaveP12(p12Data, SAVEFOLDER + "certdog.pfx");
        System.out.println("Saved to " + SAVEFOLDER + "certdog.pfx");

        client.logout();
    }

    @Test
    public void getCertJKS() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams();
        List<String> issuers = client.getIssuers();
        List<String> generators = client.getGenerators();

        ArrayList<String> sans = new ArrayList<>();
        sans.add("DNS:server1.com");
        sans.add("DNS:server2.com");
        sans.add("IP:127.0.0.1");
        sans.add("EMAIL:none@nowhere.com");

        String jksData = client.requestCert(issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client jks test", "password", sans, "From a test", null, ResponseFormat.JKS);
        System.out.println("\nCert issued OK. JKS Data:\n" + jksData);

        CertdogClient.SaveJks(jksData, SAVEFOLDER + "certdog.jks");
        System.out.println("Saved to " + SAVEFOLDER + "certdog.jks");

        client.logout();
    }

    @Test
    public void externallyManagedAuthToken() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        String authToken = client.loginExt(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams(authToken);
        List<String> issuers = client.getIssuers(authToken);
        List<String> generators = client.getGenerators(authToken);

        String pemData = client.requestCert(authToken, issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client pem test", "password", null, null, null, ResponseFormat.PEM);
        System.out.println("\nCert issued OK. PEM Data:\n" + pemData);

        String p12Data = client.requestCert(authToken, issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client pem test", "password", null, null, null, ResponseFormat.PKCS12);
        System.out.println("\nCert issued OK. P12 Data:\n" + p12Data);

        String jksData = client.requestCert(authToken, issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client pem test", "password", null, null, null, ResponseFormat.JKS);
        System.out.println("\nCert issued OK. JKS Data:\n" + jksData);


        client.logout();

    }

    @Test
    public void getCertPem() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams();
        List<String> issuers = client.getIssuers();
        List<String> generators = client.getGenerators();

        String pemData = client.requestCert(issuers.get(0), generators.get(0), teamNames.get(0),
                "CN=client pem test", "password", null, null, null, ResponseFormat.PEM);
        System.out.println("\nCert issued OK. PEM Data:\n" + pemData);

        CertdogClient.SavePem(pemData, SAVEFOLDER + "certdog.pem");
        System.out.println("Saved to " + SAVEFOLDER + "certdog.pem");

        client.logout();
    }

    @Test
    public void getCertFromCsr() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams();
        List<String> issuers = client.getIssuers();

        String csr = "-----BEGIN CERTIFICATE REQUEST-----MIICVDCCATwCAQAwDzENMAsGA1UEAwwEd2ViMzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKfEuXgAokgdH8E4osq5za33uEe6zsoYU8ctBs8j+YJBcri1oFGoxG7rn09q9cmiQmqm3eDOa3wat8tiasnvtxU0GILjjhfhawBO4A/K7k5NwXn8/2Q42M9Ni5vDJXOZ1VQZkdzwc/Eb1555FwwydrU2rg3wk2c1ijXddIXVGonIFPdnoYcg+ZqfjTOpkvMzAUMsAbhYYqu7DWNBaX6ZoB4GrvC726uYCmsrYccW6g1G+zHuZp9pPfeUGZvCdrLdNEAYsrwYyPJkUDtQ7UtpXbcXYrRQWTykAe6c+V4QDqLSpKK8EtGm3RmrTI7wgYQyfrkZPJxMveKr/Lg9N5GnmQECAwEAAaAAMA0GCSqGSIb3DQEBCwUAA4IBAQBdQx721gXBmZZG3F5P6oAAwTDiNj0ewY/xWEQf9Hc19y9gUstGDQkljWqJletEEKDu/T/KhxrT0bqJWxppZSPQ1GgDjDOGD43bOWRWNlqhCyg26KNshYaE0dVODmGjzVufBHhkeCV48CjDSrhbC2/tpg0Hkd/VVnCCQhR4IHFbZ+MXO6csY4poZ00okPbjFV6CW/SpBgR5bU2pAztSOptW7UYivMtIvwP3dXiBCMdgHsrQiUrpTBLuun6sSxShyn+21cQ+w/C11Jw/MJCzHvokd38fCUDcXjlJe96AhgPDL3PO991HgoFiCUHxFrct4bZi0gx0C3vv3IYVEIcfbvBX-----END CERTIFICATE REQUEST-----";

        X509Certificate c = client.requestCertFromCsr(issuers.get(0), teamNames.get(0), csr, null, null);
        System.out.println("\nCert issued:" + c.getSubjectDN());
        X509Certificate c2 = client.requestCertFromCsr(issuers.get(0), teamNames.get(0), csr);
        System.out.println("\nCert issued:" + c2.getSubjectDN());

        //System.out.println("\nCert issued from CSR OK. Cert Data:\n" + certData);

        CertdogClient.SaveCert(c, SAVEFOLDER + "certdog.cer");
        System.out.println("Saved to " + SAVEFOLDER + "certdog.cer");

        client.logout();
    }

    @Test
    public void getIssuerChain() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> issuers = client.getIssuers();
        List<X509Certificate> chain = client.getIssuerChain(issuers.get(0));
        for (X509Certificate c : chain)
        {
            System.out.println(c.getSubjectDN());
        }

        client.logout();
    }

    @Test
    public void revokeCert() throws Exception
    {
        CertdogClient client = new CertdogClient(APIURL);
        client.login(APIUSER, APIPASS);

        List<String> teamNames = client.getTeams();
        List<String> issuers = client.getIssuers();

        String csr = "-----BEGIN CERTIFICATE REQUEST-----MIICVDCCATwCAQAwDzENMAsGA1UEAwwEd2ViMzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAKfEuXgAokgdH8E4osq5za33uEe6zsoYU8ctBs8j+YJBcri1oFGoxG7rn09q9cmiQmqm3eDOa3wat8tiasnvtxU0GILjjhfhawBO4A/K7k5NwXn8/2Q42M9Ni5vDJXOZ1VQZkdzwc/Eb1555FwwydrU2rg3wk2c1ijXddIXVGonIFPdnoYcg+ZqfjTOpkvMzAUMsAbhYYqu7DWNBaX6ZoB4GrvC726uYCmsrYccW6g1G+zHuZp9pPfeUGZvCdrLdNEAYsrwYyPJkUDtQ7UtpXbcXYrRQWTykAe6c+V4QDqLSpKK8EtGm3RmrTI7wgYQyfrkZPJxMveKr/Lg9N5GnmQECAwEAAaAAMA0GCSqGSIb3DQEBCwUAA4IBAQBdQx721gXBmZZG3F5P6oAAwTDiNj0ewY/xWEQf9Hc19y9gUstGDQkljWqJletEEKDu/T/KhxrT0bqJWxppZSPQ1GgDjDOGD43bOWRWNlqhCyg26KNshYaE0dVODmGjzVufBHhkeCV48CjDSrhbC2/tpg0Hkd/VVnCCQhR4IHFbZ+MXO6csY4poZ00okPbjFV6CW/SpBgR5bU2pAztSOptW7UYivMtIvwP3dXiBCMdgHsrQiUrpTBLuun6sSxShyn+21cQ+w/C11Jw/MJCzHvokd38fCUDcXjlJe96AhgPDL3PO991HgoFiCUHxFrct4bZi0gx0C3vv3IYVEIcfbvBX-----END CERTIFICATE REQUEST-----";

        X509Certificate c = client.requestCertFromCsr(issuers.get(0), teamNames.get(0), csr, null, null);

        client.revokeCert(issuers.get(0), c, RevocationReason.CesationOfOperation);
        System.out.println("\nCert revoked OK\n");

        client.logout();
    }
}
