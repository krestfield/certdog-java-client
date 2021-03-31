package com.krestfield.certdog.client.model;

public class GetCertFromCsrRequest
{
    private String caName;

    private String dn;

    private String csr;

    private String teamName;

    private String extraInfo;

    private String[] extraEmails;

    public String getCaName()
    {
        return caName;
    }

    public void setCaName(String caName)
    {
        this.caName = caName;
    }

    public String getDn()
    {
        return dn;
    }

    public void setDn(String dn)
    {
        this.dn = dn;
    }

    public String getCsr()
    {
        return csr;
    }

    public void setCsr(String csr)
    {
        this.csr = csr;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public String getExtraInfo()
    {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo)
    {
        this.extraInfo = extraInfo;
    }

    public String[] getExtraEmails()
    {
        return extraEmails;
    }

    public void setExtraEmails(String[] extraEmails)
    {
        this.extraEmails = extraEmails;
    }
}
