package com.krestfield.certdog.client;

/**
 * The Certdog API URL endpoints
 */
public class CertdogEndpoints
{
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logouthere";
    public static final String MY_TEAMS = "currentuser/myteams";
    public static final String MY_ISSUERS = "currentuser/myissuers";
    public static final String CSR_GENERATORS = "admin/generators";
    public static final String CERT_REQ = "certs/request";
    public static final String CERT_JKS_DATA = "certs/%s/jks";
    public static final String CERT_PEM_DATA = "certs/%s/pem";
    public static final String CERT_REQ_CSR = "certs/requestp10";
    public static final String REVOKE_CERT = "certs/revoke";
    public static final String ISSUER_CHAIN = "admin/ca/chainbyname/%s";
}
