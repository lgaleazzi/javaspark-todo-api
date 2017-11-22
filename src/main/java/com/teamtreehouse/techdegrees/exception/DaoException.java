package com.teamtreehouse.techdegrees.exception;


public class DaoException extends RuntimeException
{
    private final Exception exception;

    public DaoException(Exception exception, String message)
    {
        super(message);
        this.exception = exception;
    }
}
