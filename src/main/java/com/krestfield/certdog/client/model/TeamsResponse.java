package com.krestfield.certdog.client.model;

public class TeamsResponse
{
    private String id;

    private String name;

    private String description;

    private String internalGroup;

    private String[] adGroups;

    private String[] authorisedCas;

    private String[] adminUsernames;

    private Boolean dontSendEmailsOnIssue;

    private Boolean dontSendReminderEmails;

    private String ipWhiteList;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getInternalGroup()
    {
        return internalGroup;
    }

    public void setInternalGroup(String internalGroup)
    {
        this.internalGroup = internalGroup;
    }

    public String[] getAdGroups()
    {
        return adGroups;
    }

    public void setAdGroups(String[] adGroups)
    {
        this.adGroups = adGroups;
    }

    public String[] getAuthorisedCas()
    {
        return authorisedCas;
    }

    public void setAuthorisedCas(String[] authorisedCas)
    {
        this.authorisedCas = authorisedCas;
    }

    public String[] getAdminUsernames()
    {
        return adminUsernames;
    }

    public void setAdminUsernames(String[] adminUsernames)
    {
        this.adminUsernames = adminUsernames;
    }

    public Boolean getDontSendEmailsOnIssue()
    {
        return dontSendEmailsOnIssue;
    }

    public void setDontSendEmailsOnIssue(Boolean dontSendEmailsOnIssue)
    {
        this.dontSendEmailsOnIssue = dontSendEmailsOnIssue;
    }

    public Boolean getDontSendReminderEmails()
    {
        return dontSendReminderEmails;
    }

    public void setDontSendReminderEmails(Boolean dontSendReminderEmails)
    {
        this.dontSendReminderEmails = dontSendReminderEmails;
    }

    public String getIpWhiteList()
    {
        return ipWhiteList;
    }

    public void setIpWhiteList(String ipWhiteList)
    {
        this.ipWhiteList = ipWhiteList;
    }
}
