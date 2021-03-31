package com.krestfield.certdog.client;

public class CertdogException extends Exception
{
    public CertdogException(String message)
    {
        super(message);
    }

    public CertdogException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
