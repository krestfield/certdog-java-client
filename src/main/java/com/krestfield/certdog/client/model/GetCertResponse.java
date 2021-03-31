package com.krestfield.certdog.client.model;

public class GetCertResponse
{
    private String certId;
    private String p12Data;

    public String getCertId()
    {
        return certId;
    }

    public void setCertId(String certId)
    {
        this.certId = certId;
    }

    public String getP12Data()
    {
        return p12Data;
    }

    public void setP12Data(String p12Data)
    {
        this.p12Data = p12Data;
    }
}
