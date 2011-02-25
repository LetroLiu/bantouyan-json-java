package com.bantouyan.json;

/**
 * 运行时异常，当对Json数据进行了错误的类型转换时，或者不打算覆盖JsonObject 
 * Name 对应的Value时重设已有的Name对应的Value，或者构造的Json
 * 实例含有循环引用导致无法转换为Json文本，就应该抛出此异常。
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public class JsonException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 创建JsonException。
     */
    protected JsonException()
    {
        super();
    }
    
    /**
     * 创建JsonException。
     * @param msg 对异常的描述
     */
    protected JsonException(String msg)
    {
        super(msg);
    }
    
    /**
     * 创建JsonException。
     * @param e 源异常
     */
    protected JsonException(Exception e)
    {
        super(e);
    }
    
    /**
     * 创建JsonException。
     * @param msg 对异常的描述
     * @param e 源异常
     */
    protected JsonException(String msg, Exception e)
    {
        super(msg, e);
    }
}
