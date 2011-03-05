package com.bantouyan.json;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * <p>用来表示STRING、NUMBER（LONG与DOUBLE）、BOOLEAN与NULL类型的Json实例，
 * 创建后不可更改，且无需显式调用构造函数创建。</p>
 * 
 * <p>方法<strong>getXXX</strong>以特定的类型返回Json实例的值，
 * 方法<strong>canToXXX</strong>判定Json实例值是否可以转换为这种类型。
 * 方法<strong>getType</strong>返回Json实例的类型，
 * 方法<strong>isEmpty、count、clear</strong>仅仅是从超类Json继承来的占位符，
 * 无实际意义。</p>
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public final class JsonPrimitive extends Json
{
    private JsonType type = null;
    private Object data;
    
    /**
     * 创建类型为NULL的Json实例。（只有一个值null）
     */
    public JsonPrimitive()
    {
        this.type = JsonType.NULL;
        this.data = "null";
    }
    
    /**
     * 创建浮点型的Json实例。（因调用限制在此包内，所以不处理参数为null的情况）
     * @param data 创建Json的浮点型数值，如果是NaN或Infinity，则转换为字符串型
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
     * 创建整型的Json实例。（因调用限制在此包内，所以不处理参数为null的情况）
     * @param data 创建Json的整型数值
     */
    public JsonPrimitive(Long data)
    {
        this.type = JsonType.INTEGER;
        this.data = data;
    }

    /**
     * 创建数值型（整型或浮点型）Json实例。（因调用限制在此包内，所以不处理参数为null的情况）
     * @param data 创建Json的数值，如果是NaN或Infinity，则转换为字符串型
     */
    public JsonPrimitive(Number data)
    {
        if(data instanceof Long)
        {
            this.type = JsonType.INTEGER;
            this.data = data;
        }
        else if(data instanceof Double)
        {
            if(((Double)data).isNaN() || ((Double)data).isInfinite())
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
        else if((data instanceof BigDecimal) || (data instanceof Float))
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
        else // other integer type
        {
            this.type = JsonType.INTEGER;
            this.data = (Long)data.longValue();
        }
    }
    
    /**
     * 创建字符串型Json实例。（因会被嵌套调用到参数为null的情况，所以必须处理）
     * @param data 创建Json的字符串
     */
    public JsonPrimitive(String data)
    {
        if(data == null)
        {
            this.type = JsonType.NULL;
            this.data = "null";
        }
        else
        {
            this.type = JsonType.STRING;
            this.data = data;
        }
    }
    
    /**
     * 创建逻辑型（布尔型）Json实例。(因调用限制在此包内，所以不处理参数为null的情况)
     * @param data 创建Json的逻辑型（布尔型）值
     */
    public JsonPrimitive(Boolean data)
    {
        this.type = JsonType.BOOLEAN;
        this.data = data;
    }
    
    /**
     * 返回Json 实例的字符串值。
     * @return 返回对应的字符串，不带引号与转义符
     */
    public String getString()
    {
        return this.data.toString();
    }
    
    /**
     * 判断Json实例是否可以转换为逻辑型（布尔型）值，如果类型是BOOLEAN，
     * 或是类型是STRING且值为true或false（不区分大小写）， 则认为可以转换。
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
     * 返回Json实例的逻辑型（布尔型）值。
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
     * 判断Json实例是否可以转换为整型值，如果类型是INTEGER，
     * 或类型是STRING且其值表示一个整数，则认为可以转换。
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
            return (str.matches("\\s*[+-]?\\d+\\s*"))? true: false;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回Json实例的整型值。
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
                String str = this.data.toString().trim();
                if(str.charAt(0) == '+')
                    str = str.substring(1);
                value = Long.parseLong(str);
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
     * 判断Json实例是否可以转换为浮点型值，如果类型是INTEGER或FLOAT，
     * 或者是STRING且其值表示一个数值，则认为可以转换。
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
     * 返回Json实例的浮点型值。
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
        else if(this == obj)
        {
            return true;
        }
        else if(this.type != obj.getType())
        {
            return false;
        }
        else if(this.type == JsonType.NULL)
        {
            return true;
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
     * Clone一个JsonPrimitive实例。
     * @return Clone出来的JsonPrimitive实例
     */
    @Override
    public JsonPrimitive clone()
    {
        //返回JsonPrimitive实例本身，
        //之所以这样设计是因为JsonPrimitive是不变对象，没有必要clone。
        //但是其超类Json实现了clone方法，为了避免不必要的clone操作,
        //所以重写了这个方法。
        return this;
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
     * 清空Json实例的所有子元素，对Primitive类型的Json实例无意义，故不做任何操作。
     */
    @Override
    public void clear()
    {
    }
    
    /**
     * 判断Json实例子元素的个数是否为零，对Primitive类型的Json实例无意义，返回true。
     * @return 恒为true
     */
    @Override
    public boolean isEmpty()
    {
        return false;
    }

    /**
    * 向可追加对象追加Json文本。
    * @param dest 接受Json文本的可追加对象
    * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
    * @throws IOException 追加字符流发生IO异常
    */
    @Override
   protected void appendToAppendable(Appendable dest, boolean useQuote) throws IOException
    {
        if(this.type == JsonType.STRING)
        {
            JsonTextParser.jsonStringToAppendable((String)data, dest);
        }
        else
        {
            dest.append(data.toString());
        }
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
     * 判断Json实例内是否存在循环引用，对Primitive类型的Json实例无意义，返回false。
     * @param parentRef 上级Json对象的引用
     * @return false
     */
    @Override
    protected boolean existsCircle(IdentityStack parentRef)
    {
        return false;
    }
}
