package com.krestfield.certdog.client.model;

public class GetCertFromCsrResponse
{
    private String id;
    private String pemCert;

    public String getPemCert()
    {
        return pemCert;
    }

    public void setPemCert(String pemCert)
    {
        this.pemCert = pemCert;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
