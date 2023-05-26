package com.krestfield.certdog.client.model;

public class GetCertResponse
{
    private String id;

    private String pemCert;
    private String p12Data;

    public String getP12Data()
    {
        return p12Data;
    }

    public void setP12Data(String p12Data)
    {
        this.p12Data = p12Data;
    }

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
