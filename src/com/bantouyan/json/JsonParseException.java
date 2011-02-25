package com.bantouyan.json;

/**
 * 解析Json字符串、Reader或者Java对象时如果源数据格式错误，则抛出该异常。
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public class JsonParseException extends Exception
{
    private static final long serialVersionUID = 1L;

    /**
     * 创建JsonException。
     */
    protected JsonParseException()
    {
        super();
    }
    
    /**
     * 创建JsonException。
     * @param msg 对异常的描述
     */
    protected JsonParseException(String msg)
    {
        super(msg);
    }
    
    /**
     * 创建JsonException。
     * @param e 源异常
     */
    protected JsonParseException(Exception e)
    {
        super(e);
    }
    
    /**
     * 创建JsonException。
     * @param msg 对异常的描述
     * @param e 源异常
     */
    protected JsonParseException(String msg, Exception e)
    {
        super(msg, e);
    }
}
