package com.krestfield.certdog.client.model;

public class GeneratorsResponse
{
    private String id;
    private String name;
    private String signatureAlgorithm;
    private String keySize;
    private String ellipticCurveName;
    private String hashAlgorithm;

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

    public String getSignatureAlgorithm()
    {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm)
    {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getKeySize()
    {
        return keySize;
    }

    public void setKeySize(String keySize)
    {
        this.keySize = keySize;
    }

    public String getEllipticCurveName()
    {
        return ellipticCurveName;
    }

    public void setEllipticCurveName(String ellipticCurveName)
    {
        this.ellipticCurveName = ellipticCurveName;
    }

    public String getHashAlgorithm()
    {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm)
    {
        this.hashAlgorithm = hashAlgorithm;
    }
}
