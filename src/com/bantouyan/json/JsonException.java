package com.bantouyan.json;

public class JsonException extends Exception
{
    private static final long serialVersionUID = 1L;

    protected JsonException()
    {
        super();
    }
    
    protected JsonException(String msg)
    {
        super(msg);
    }
    
    protected JsonException(Exception e)
    {
        super(e);
    }
    
    protected JsonException(String msg, Exception e)
    {
        super(msg, e);
    }
}
