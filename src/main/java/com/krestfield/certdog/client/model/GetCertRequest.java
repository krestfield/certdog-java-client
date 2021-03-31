package com.krestfield.certdog.client.model;

public class GetCertRequest
{
    private String caName;

    private String dn;

    private String teamName;

    private String[] subjectAltNames;

    private String csrGeneratorName;

    private String p12Password;

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

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public String[] getSubjectAltNames()
    {
        return subjectAltNames;
    }

    public void setSubjectAltNames(String[] subjectAltNames)
    {
        this.subjectAltNames = subjectAltNames;
    }

    public String getCsrGeneratorName()
    {
        return csrGeneratorName;
    }

    public void setCsrGeneratorName(String csrGeneratorName)
    {
        this.csrGeneratorName = csrGeneratorName;
    }

    public String getP12Password()
    {
        return p12Password;
    }

    public void setP12Password(String p12Password)
    {
        this.p12Password = p12Password;
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
