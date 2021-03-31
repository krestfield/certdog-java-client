package com.krestfield.certdog.client;

import com.krestfield.certdog.client.model.GetCertFromCsrRequest;
import com.krestfield.certdog.client.model.GetCertFromCsrResponse;
import com.krestfield.certdog.client.model.GetCertRequest;
import com.krestfield.certdog.client.model.GeneratorsResponse;
import com.krestfield.certdog.client.model.GetCertResponse;
import com.krestfield.certdog.client.model.LoginRequest;
import com.krestfield.certdog.client.model.LoginResponse;
import com.krestfield.certdog.client.model.RevokeCertRequest;
import com.krestfield.certdog.client.model.TeamsResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CertdogClient
 *
 * Java client to interface with the certdog application to obtain certificates from CSR
 * or by supplying a DN and referencing a CSR Generator
 *
 * Methods are also included to revoke as well as obtain the available issuers, teams and csr generators
 *
 * https://www.krestfield.com/certdog
 * support@krestfield.com
 *
 * Copyright (c) 2021, Krestfield Limited
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *     and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *   * Neither the name of Krestfield Limited nor the names of its contributors may be used to endorse or
 *     promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class CertdogClient
{
    String authToken = null;
    WebTarget target = null;
    boolean loggedIn = false;

    public static final String CERT_HEADER = "-----BEGIN CERTIFICATE-----";
    public static final String CERT_FOOTER = "-----END CERTIFICATE-----";

    public enum RevocationReason
    {
        Unspecified(0),
        KeyCompromise(1),
        CaCompromise(2),
        AffiliationChanged(3),
        Superseded(4),
        CesationOfOperation(5),
        Hold(6);
        private final int value;

        RevocationReason(final int newValue) {
            value = newValue;
        }
    }

    HashMap<Integer, String> revokeReasons = new HashMap<Integer, String>(){{
        put(RevocationReason.Unspecified.value, "unspecified");
        put(RevocationReason.KeyCompromise.value, "key compromise");
        put(RevocationReason.CaCompromise.value, "ca compromise");
        put(RevocationReason.AffiliationChanged.value, "affiliation changed");
        put(RevocationReason.Superseded.value, "superseded");
        put(RevocationReason.CesationOfOperation.value, "cessation of operation");
        put(RevocationReason.Hold.value, "hold");
    }};

    public enum ResponseFormat
    {
        PKCS12,
        JKS,
        PEM
    }

    /**
     * Constructor
     *
     * @param apiUrl - the certdog API URL
     */
    public CertdogClient(String apiUrl)
    {
        Client client = ClientBuilder.newClient();
        this.target = client.target(apiUrl);
    }

    /**
     * Login to the API
     * This will retain the JWT for future calls
     *
     * @param username - the certdog username
     * @param password - the certdog password
     * @throws CertdogException
     */
    public void login(String username, String password) throws CertdogException
    {
        try
        {
            loggedIn = false;
            LoginRequest login = new LoginRequest();
            login.setUsername(username);
            login.setPassword(password);

            LoginResponse resp = target
                    .path(CertdogEndpoints.LOGIN)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(login, MediaType.APPLICATION_JSON), LoginResponse.class);

            this.authToken = resp.getToken();
            loggedIn = true;
        }
        catch (Exception e)
        {
            throw new CertdogException("Login to the certdog API failed. " + e.getMessage());
        }
    }

    /**
     * Logout of the certdog API
     *
     * @throws CertdogException
     */
    public void logout() throws CertdogException
    {
        if (!loggedIn)
            return;

        Response resp = target.path(CertdogEndpoints.LOGOUT)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authToken)
                .get(Response.class);

        if (resp.getStatus() != 200)
        {
            throw new CertdogException(resp.readEntity(String.class));
        }
    }

    /**
     * Returns a list of teams that the current logged in user
     * is a member of
     *
     * @return
     * @throws CertdogException
     */
    public List<String> getTeams() throws CertdogException
    {
        if (!loggedIn)
            throw new CertdogException("Not logged in. Call login");

        List<TeamsResponse> teams = target
                .path(CertdogEndpoints.MY_TEAMS)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authToken)
                .get(new GenericType<List<TeamsResponse>>(){});

        List<String> teamNames = teams.stream().map(x -> x.getName()).collect(Collectors.toList());

        return teamNames;
    }

    /**
     * Returns a list of issuer names that the current logged in user
     * has permissions to request certificates from
     *
     * @return
     * @throws CertdogException
     */
    public List<String> getIssuers() throws CertdogException
    {
        if (!loggedIn)
            throw new CertdogException("Not logged in. Call login");

        List<String> issuerNames = target
                .path(CertdogEndpoints.MY_ISSUERS)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authToken)
                .get(new GenericType<List<String>>(){});

        return issuerNames;
    }

    /**
     * Returns a list of CSR generators that can be referenced when
     * requesting a certificate
     *
     * @return
     * @throws CertdogException
     */
    public List<String> getGenerators() throws CertdogException
    {
        if (!loggedIn)
            throw new CertdogException("Not logged in. Call login");

        List<GeneratorsResponse> generators = target
                .path(CertdogEndpoints.CSR_GENERATORS)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + authToken)
                .get(new GenericType<List<GeneratorsResponse>>(){});

        List<String> generatorNames = generators.stream().map(x -> x.getName()).collect(Collectors.toList());

        return generatorNames;
    }

    /**
     * Request a certificate from a DN
     * Note: This will use certdog to generate a CSR. If you want to generate the CSR locally and send to
     * certdog for processing, use the RequestCertFromCsr method instead
     *
     * @param issuerName the cert issuer to process the request
     * @param generatorName the CSR generator to create the CSR
     * @param teamName the team this certificate will be associated with
     * @param dn the requested Dn
     * @param password the password that will protect the P12/JKS/PEM
     * @param sans An array of Subject Alternative Names. In the form DNS:[dns name],EMAIL:[email],IP:[ip address]
     * @param extraInfo Any extra free text to be associated with the certificate
     * @param extraEmails Additional emails to send renewal reminders and issue emails
     * @param format The return format - PKCS12/JKS or PEM
     * @return the PKCS12/PFX data, base64 encoded. Use SaveP12 to save to a PFX/P12 file for import
     * @throws CertdogException
     */
    public String requestCert(String issuerName, String generatorName, String teamName,
                              String dn, String password, List<String> sans,
                              String extraInfo, List<String> extraEmails, ResponseFormat format) throws CertdogException
    {
        if (!loggedIn)
            throw new CertdogException("Not logged in. Call login");

        try
        {
            GetCertRequest certReq = new GetCertRequest();
            certReq.setCaName(issuerName);
            certReq.setCsrGeneratorName(generatorName);
            certReq.setDn(dn);
            certReq.setSubjectAltNames(sans != null ? sans.toArray(new String[]{}) : null);
            certReq.setTeamName(teamName);
            certReq.setExtraInfo(extraInfo);
            certReq.setExtraEmails(extraEmails != null ? extraEmails.toArray(new String[]{}) : null);
            certReq.setP12Password(password);

            GetCertResponse resp = target
                    .path(CertdogEndpoints.CERT_REQ)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .post(Entity.entity(certReq, MediaType.APPLICATION_JSON), GetCertResponse.class);

            // P12 data is returned by default. If we want another format we make a separate GET call
            // to get the required format
            if (format == ResponseFormat.PKCS12)
                return resp.getP12Data();
            else
            {
                // Provide the certId to get the data in the required format
                String urlEndPoint = CertdogEndpoints.CERT_JKS_DATA;
                if (format == ResponseFormat.PEM)
                    urlEndPoint = CertdogEndpoints.CERT_PEM_DATA;
                String path = String.format(urlEndPoint, resp.getCertId());

                String data = target
                        .path(path)
                        .request(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + authToken)
                        .get(String.class);

                return data;
            }
        }
        catch (Exception e)
        {
            throw new CertdogException("Requesting certificate with DN '" + dn + "' failed. " + e.getMessage());
        }
    }

    /**
     * Requests a certificate from a pre-generated CSR
     *
     * @param issuerName the cert issuer to process the request
     * @param teamName the team this certificate will be associated with
     * @param csrData the CSR data
     * @param extraInfo Any extra free text to be associated with the certificate
     * @param extraEmails Additional emails to send renewal reminders and issue emails
     * @return the PEM formatted certificate. Use GetCertFromData to get a X509Certificate and SaveCert to save
     * @throws CertdogException
     */
    public String requestCertFromCsr(String issuerName, String teamName, String csrData,
                                     String extraInfo, List<String> extraEmails) throws CertdogException
    {
        if (!loggedIn)
            throw new CertdogException("Not logged in. Call login");

        try
        {
            GetCertFromCsrRequest certReq = new GetCertFromCsrRequest();
            certReq.setCaName(issuerName);
            certReq.setCsr(csrData);
            certReq.setTeamName(teamName);
            certReq.setExtraInfo(extraInfo);
            certReq.setExtraEmails(extraEmails != null ? extraEmails.toArray(new String[]{}) : null);

            GetCertFromCsrResponse resp = target
                    .path(CertdogEndpoints.CERT_REQ_CSR)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .post(Entity.entity(certReq, MediaType.APPLICATION_JSON), GetCertFromCsrResponse.class);

            return resp.getPemCert();
        }
        catch (Exception e)
        {
            throw new CertdogException("Requesting certificate from CSR failed. " + e.getMessage());
        }
    }

    /**
     * Revokes a cert given the certificate and issuer
     *
     * @param issuerName the cert issuer name - must be the same as the cert was issued from
     * @param cert the certificate
     * @param reason the revocation reason
     * @throws CertdogException
     */
    public void revokeCert(String issuerName, X509Certificate cert, RevocationReason reason) throws CertdogException
    {
        revokeCert(issuerName, cert.getSerialNumber().toString(16), reason);
    }

    /**
     * Revokes a cert given the certificate serial number and issuer
     *
     * @param issuerName the cert issuer name - must be the same as the cert was issued from
     * @param serialNumber the serial number in ASCII HEX format
     * @param reason the revocation reason
     * @throws CertdogException
     */
    public void revokeCert(String issuerName, String serialNumber, RevocationReason reason) throws CertdogException
    {
        if (!loggedIn)
            throw new CertdogException("Not logged in. Call login");

        try
        {
            RevokeCertRequest revokeReq = new RevokeCertRequest();
            revokeReq.setCaName(issuerName);
            revokeReq.setSerialNumber(serialNumber);
            revokeReq.setReason(revokeReasons.get(reason.value));

            Response resp = target
                    .path(CertdogEndpoints.REVOKE_CERT)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + authToken)
                    .post(Entity.entity(revokeReq, MediaType.APPLICATION_JSON), Response.class);

            if (resp.getStatus() != 200)
            {
                throw new Exception(resp.readEntity(String.class));
            }
        }
        catch (Exception e)
        {
            throw new CertdogException("Revocation of certificate with serial number " + serialNumber + " failed. " + e.getMessage());
        }

    }

    /**
     * Saves the data returned from RequestCert as a file - this converts the data to binary
     * which is required for PFX files
     *
     * @param p12B64Data the base64 p12/pfx data
     * @param filename the filename to save the data
     * @throws CertdogException
     */
    public static void SaveP12(String p12B64Data, String filename) throws CertdogException
    {
        try
        {
            byte[] p12BinData = Base64.getDecoder().decode(p12B64Data);
            Files.write(Paths.get(filename), p12BinData);
        }
        catch (Exception e)
        {
            throw new CertdogException("Unable to save p12/jks data to file: " + filename + " Error: " + e.getMessage());
        }
    }

    /**
     * Saves the data returned from RequestCert as a file - this converts the data to binary
     * which is required for JKS files
     *
     * @param jksB64Data the base64 encoded JKS data
     * @param filename the filename to save the data
     * @throws CertdogException
     */
    public static void SaveJks(String jksB64Data, String filename) throws CertdogException
    {
        SaveP12(jksB64Data, filename);
    }

    /**
     * Saves the data returned from RequestCert as a file
     *
     * @param pemData the PEM data
     * @param filename the filename to save the data
     * @throws CertdogException
     */
    public static void SavePem(String pemData, String filename) throws CertdogException
    {
        try
        {
            Files.write(Paths.get(filename), pemData.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            throw new CertdogException("Unable to save PEM data to file: " + filename + " Error: " + e.getMessage());
        }
    }

    /**
     * Saves the data as returned from RequestCertFromCsr to a file
     *
     * @param certData the certificate data
     * @param filename the filename to save the data
     * @throws CertdogException
     */
    public static void SaveCert(String certData, String filename) throws CertdogException
    {
        try
        {
            Files.write(Paths.get(filename), certData.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            throw new CertdogException("Unable to save cert data to file: " + filename + " Error: " + e.getMessage());
        }
    }

    /**
     * Given the certificate data returns a X509Certificate object
     *
     * @param certData The cert data
     * @return A certificate object
     * @throws CertdogException
     */
    public static X509Certificate GetCertFromData(String certData) throws CertdogException
    {
        try
        {
            certData = certData.replace(CERT_HEADER, "");
            certData = certData.replace(CERT_FOOTER, "");
            certData = certData.replace("\r", "");
            certData = certData.replace("\n", "");
            certData = certData.replace(" ", "");
            byte[] binaryCertData = Base64.getDecoder().decode(certData);
            InputStream is = new ByteArrayInputStream(binaryCertData);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);

            return cert;
        }
        catch (Exception e)
        {
            throw new CertdogException("Unable to create a certificate from the certificate data provided. " + e.getMessage());
        }
    }
}
