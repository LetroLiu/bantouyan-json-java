package com.bantouyan.json;

/**
 * 运行时异常，当被解析的Json字符串、Reader或者Java对象等源数据格式错误时，
 * 或者构造的Json实例含有循环引用导致无法转换为Json文本，
 * 或者向JsonObject增加一个新元素而新元素的Name已经存在时（即不打算覆盖已有Name的Value时），
 * 或者获取Json子元素值时使用了不合适的类型，就应该抛出此异常。
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
