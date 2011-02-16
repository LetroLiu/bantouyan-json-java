package com.bantouyan.json;

/**
 * Json异常，如果因为源数据格式问题无法从文本或Java对象解析为Json实例，
 * 或无法对Json实例执行操作（如修改、生成Json文本等），则应该抛出此异常
 * @author bantouyan
 * @version 1.00
 */
public class JsonException extends Exception
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
