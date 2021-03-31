package com.krestfield.certdog.client.model;

public class RevokeCertRequest
{
    private String serialNumber;
    private String reason;
    private String caName;

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getCaName()
    {
        return caName;
    }

    public void setCaName(String caName)
    {
        this.caName = caName;
    }
}
