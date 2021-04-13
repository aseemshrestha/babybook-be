package com.babybook.email.exceptions;

public class DataNotAvailableException extends RuntimeException
{
    public DataNotAvailableException(String message)
    {
        super(message);
    }
}
