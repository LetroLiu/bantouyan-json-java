package com.bantouyan.json;

import java.math.BigDecimal;

/**
 * 用来表示String、Number、Boolean与null类型的Json实例，创建后不可更改。
 * @author bantouyan
 * @version 0.1
 */
public class JsonPrimitive extends Json
{
    private JsonType type = null;
    private Object data;
    
    /**
     * 创建值为null的Json实例
     */
    protected JsonPrimitive()
    {
        this.type = JsonType.NULL;
        this.data = "null";
    }
    
    /**
     * 创建浮点型的Json实例。
     * @param data
     */
    protected JsonPrimitive(Double data)
    {
        this.type = JsonType.FLOAT;
        this.data = data;
    }

    /**
     * 创建整型的Json实例。
     * @param data
     */
    protected JsonPrimitive(Long data)
    {
        this.type = JsonType.INTEGER;
        this.data = data;
    }
    
    /**
     * 创建数值型（整型或浮点型）Json实例。
     * @param data
     */
    protected JsonPrimitive(Number data)
    {
        if((data instanceof BigDecimal) 
            || (data instanceof Float) 
            || (data instanceof Double))
        {
            this.type = JsonType.FLOAT;
        }
        else
        {
            this.type = JsonType.INTEGER;
        }
        
        this.data = data;
    }
    
    /**
     * 创建字符串型Json实例。
     * @param data
     */
    protected JsonPrimitive(String data)
    {
        this.type = JsonType.STRING;
        this.data = data;
    }
    
    /**
     * 创建逻辑型（布尔型）Json实例。
     * @param data
     */
    protected JsonPrimitive(Boolean data)
    {
        this.type = JsonType.BOOLEAN;
        this.data = data;
    } 

    /**
     * 生成Json文本。
     * @param useStandard 无意义，被忽略
     * @return
     */
    @Override
    protected String generateJsonTextWithoutCheck(boolean useStandard)
    {
        String str = data.toString();
        if(this.type == JsonType.STRING)
        {
            str = JsonTextParser.toJsonString(str);
        }
        return str;
    }

    /**
     * 返回 Json实例类型STRING, INTEGER, FLOAT, BOOLEAN或NULL。
     */
    @Override
    public JsonType getType()
    {
        return type;
    }
    
    /**
     * 返回Json 实例的字符串值。
     * @return
     */
    @Override
    public String getString()
    {
        return this.data.toString();
    }
    
    /**
     * 返回逻辑型（布尔型）值。
     * @return
     * @throws JsonException
     */
    @Override
    public boolean getBoolean() throws JsonException
    {
        if(this.type == JsonType.BOOLEAN)
        {
            return (Boolean)this.data;
        }
        else if(this.type == JsonType.STRING)
        {
            String str = this.data.toString();
            if(str.equals("true"))
            {
                return true;
            }
            else if(str.equals("false"))
            {
                return false;
            }
            else
            {
                throw new JsonException("Cannot transfer String value \"" + str + "\" to boolean value.");
            }
        }
        else
        {
            throw new JsonException("Cannot transfer to boolean value for type is not BOOLEAN.");
        }
    }
    
    /**
     * 返回整型值。
     * @return
     * @throws JsonException
     */
    @Override
    public long getLong() throws JsonException
    {
        if(this.type == JsonType.INTEGER)
        {
            return (Long)this.data;
        }
        else if(this.type == JsonType.STRING)
        {
            Long value = null;
            try
            {
                value = Long.parseLong(this.data.toString());
            } 
            catch (NumberFormatException e)
            {
                throw new JsonException("Cannot transfer String \"" + this.data.toString() + "\" to integer value.");
            }
            return value;
        }
        else
        {
            throw new JsonException("Cannot transfer to long value for type is not INTEGER.");
        }
    }
    
    /**
     * 返回浮点型值。
     * @return
     * @throws JsonException
     */
    @Override
    public double getDouble() throws JsonException
    {
        if(this.type == JsonType.FLOAT)
        {
            return (Double)this.data;
        }
        else if(this.type == JsonType.INTEGER)
        {
            long value = (Long)this.data;
            return (double)value;
        }
        else if(this.type == JsonType.STRING)
        {
            Double value = null;
            try
            {
                value = Double.parseDouble(this.data.toString());
            } 
            catch (NumberFormatException e)
            {
                throw new JsonException("Cannot transfer String \"" + this.data.toString() + "\" to float value.");
            }
            return value;
        }
        else
        {
            throw new JsonException("Cannot transfer to double value for type is not FLOAT.");
        }
    }

    /**
     * 无意义，不做任何操作。
     */
    @Override
    public void clear()
    {
    }

    /**
     * 无意义，返回false。
     */
    @Override
    public boolean isEmpty()
    {
        return false;
    }

    /**
     * 无意义，返回1。
     */
    @Override
    public int count()
    {
        return 1;
    }

    /**
     * 判断Json对象内是否存在循环引用
     * @param parentRef 上级Json对象的引用
     * @return
     */
    @Override
    protected boolean existsCircle(IdentityStack parentRef)
    {
        return false;
    }
}
