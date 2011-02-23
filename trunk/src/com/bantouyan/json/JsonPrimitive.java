package com.bantouyan.json;

import java.math.BigDecimal;

/**
 * <p>用来表示String、Number、Boolean与null类型的Json实例，
 * 创建后不可更改，且无需显示创建。</p>
 * 
 * <p>方法<strong>getXXX</strong>获取Json实例某种原始类型的值，方法
 * <strong>canToXXX</strong>判定Json实例值是否可以转换为这种原始类型。方法
 * <strong>getType</strong>返回Json实例的类型，方法<strong>isEmpty、count、
 * clear</strong>仅仅是从超类Json继承来的占位符，无实际意义。</p>
 * 
 * @author bantouyan
 * @version 1.00
 */
public class JsonPrimitive extends Json
{
    private JsonType type = null;
    private Object data;
    
    /**
     * 创建值为null的Json实例。
     */
    protected JsonPrimitive()
    {
        this.type = JsonType.NULL;
        this.data = "null";
    }
    
    /**
     * 创建浮点型的Json实例。
     * @param data 创建Json的浮点型数值，如果是NaN或Infinity，则转换为字符串
     */
    public JsonPrimitive(Double data) throws JsonException
    {
        if(data.isNaN() || data.isInfinite())
        {
            this.type = JsonType.STRING;
            this.data = data.toString();
        }
        else
        {
            this.type = JsonType.FLOAT;
            this.data = data;
        }
    }

    /**
     * 创建整型的Json实例。
     * @param data 创建Json的整型数值
     */
    protected JsonPrimitive(Long data)
    {
        this.type = JsonType.INTEGER;
        this.data = data;
    }

    /**
     * 创建数值型（整型或浮点型）Json实例。
     * @param data 创建Json的数值，如果是NaN或Infinity，则转换为字符串
     */
    public JsonPrimitive(Number data)
    {
        if((data instanceof BigDecimal) 
            || (data instanceof Float) 
            || (data instanceof Double))
        {
            double dv = data.doubleValue();
            if(Double.isNaN(dv) || Double.isInfinite(dv))
            {
                this.type = JsonType.STRING;
                this.data = data.toString();
            }
            else
            {
                this.type = JsonType.FLOAT;
                this.data = (Double)data.doubleValue();
            }
        }
        else
        {
            this.type = JsonType.INTEGER;
            this.data = (Long)data.longValue();
        }
    }
    
    /**
     * 创建字符串型Json实例。
     * @param data 创建Json的字符串
     */
    protected JsonPrimitive(String data)
    {
        this.type = JsonType.STRING;
        this.data = data;
    }
    
    /**
     * 创建逻辑型（布尔型）Json实例。
     * @param data 创建Json的逻辑型（布尔型）值
     */
    protected JsonPrimitive(Boolean data)
    {
        this.type = JsonType.BOOLEAN;
        this.data = data;
    }

    /**
     * 生成Json文本，并追加到参数builder的尾部
     * @param builder 保存Json文本的StringBuilder
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号，但此类中无意义
     */
    @Override
    protected void generateJsonText(StringBuilder builder, boolean useQuote)
    {
        String str = data.toString();
        if(this.type == JsonType.STRING)
        {
            str = JsonTextParser.toJsonString(str);
        }
        builder.append(str);
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
     * @return 返回对应的字符串，不带引号
     */
    public String getString()
    {
        return this.data.toString();
    }
    
    /**
     * 判断是否可以转换为逻辑型（布尔型）值
     * @return 是返回true，否则返回false
     */
    public boolean canToBoolean()
    {
        if(this.type == JsonType.BOOLEAN)
        {
            return true;
        }
        else if(this.type == JsonType.STRING)
        {
            String str = this.data.toString();
            str = str.trim().toLowerCase();
            return (str.equals("true") || str.equals("false"))? true: false;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回逻辑型（布尔型）值，当Json类型不是BOOLEAN也不是字符串true、false时抛出异常。
     * @return 对应的逻辑型（布尔型）值
     * @throws JsonException 无法转换为逻辑型值
     */
    public boolean getBoolean() throws JsonException
    {
        if(this.type == JsonType.BOOLEAN)
        {
            return (Boolean)this.data;
        }
        else if(this.type == JsonType.STRING)
        {
            String str = this.data.toString();
            str = str.trim().toLowerCase();
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
     * 判断是否可以转换为整型值。
     * @return 是返回true，否则返回false
     */
    public boolean canToLong()
    {
        if(this.type == JsonType.INTEGER)
        {
            return true;
        }
        else if(this.type == JsonType.STRING)
        {
            String str = this.data.toString();
            return (str.matches("\\s*-?\\d+\\s*"))? true: false;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回整型值，当Json类型不是INTEGER也不是可以转换为整型数值的字符串时抛出异常。
     * @return 对应的整型值
     * @throws JsonException 无法转换为整型值
     */
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
                value = Long.parseLong(this.data.toString().trim());
            } 
            catch (NumberFormatException e)
            {
                throw new JsonException("Cannot transfer String \"" + this.data.toString() + "\" to long value.");
            }
            return value;
        }
        else
        {
            throw new JsonException("Cannot transfer to long value for type is not INTEGER.");
        }
    }
    
    /**
     * 判断是否可以转换为浮点型值。
     * @return 是返回true，否则返回false
     */
    public boolean canToDouble()
    {
        if(this.type == JsonType.FLOAT || this.type == JsonType.INTEGER)
        {
            return true;
        }
        else if(this.type == JsonType.STRING)
        {
            String str = this.data.toString();
            return (str.matches("\\s*[+-]?\\d+(\\.\\d*)?([eE][+-]?\\d+)?\\s*"))? true: false;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回浮点型值，当Json类型不是FLOAT也不是可以转换为浮点型数值的字符串时抛出异常。
     * @return 对应的浮点型值
     * @throws JsonException 无法转换为浮点型值
     */
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
                throw new JsonException("Cannot transfer String \"" + this.data.toString() + "\" to double value.");
            }
            return value;
        }
        else
        {
            throw new JsonException("Cannot transfer to double value for type is not FLOAT.");
        }
    }

    /**
     * 判断两个Json实例表示的数据是否一致。
     * @param obj 被比较的Json实例
     * @return 一致返回true，不一致返回false
     */
    @Override
    protected boolean same(Json obj)
    {
        if(obj == null)
        {
            return false;
        }
        else if(this.type != obj.getType())
        {
            return false;
        }
        else if(this.type == JsonType.FLOAT)
        {
            Double tv = (Double)this.data;
            Double ov = (Double)((JsonPrimitive)obj).data;
            if(tv.isNaN() && ov.isNaN())
            {
                return true;
            }
            else
            {
                double td = tv;
                double od = ov;
                return td == od;
            }
        }
        else
        {
            return this.data.equals(((JsonPrimitive)obj).data);
        }
    }
    
    /**
     * JsonPrimitive实例的hash值，即类型的hashCode与数据的hashCode的和。
     * @return 根据对应的标准Json文本生成hash值
     */
    @Override
    public int hashCode()
    {
        return type.hashCode() + data.hashCode();
    }

    /**
     * 清空Json实例的所有子元素，对Primitive类型的Json实例无意义，故不做任何操作。
     */
    @Override
    public void clear()
    {
    }
    
    /**
     * Json实例是否包含子元素，对Primitive类型的Json实例无意义，返回true。
     * @return true
     */
    @Override
    public boolean isEmpty()
    {
        return false;
    }
    
    /**
     * Json实例子元素的个数，对Primitive类型的Json实例无意义，返回0。
     * @return 0
     */
    @Override
    public int count()
    {
        return 0;
    }
    
    /**
     * 判断Json实例内是否存在循环引用，对Primitive类型的Json实例无意义，返回false
     * @param parentRef 上级Json对象的引用
     * @return false
     */
    @Override
    protected boolean existsCircle(IdentityStack parentRef)
    {
        return false;
    }
}
