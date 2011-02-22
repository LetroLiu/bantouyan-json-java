package com.bantouyan.json;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>所有Json实例的超类，用来提供操作Json的通用接口。</p>
 * 
 * <p><strong>生成Json实例</strong>，把文本转换为Json实例可以调用Json的类方法
 * parseJsonText或parseJsonReader，如果想从Java集合生成Json实例，则可以调用
 * Json的类方法parseJavaMap或parseJavaCollection。</p>
 * 
 * <p><strong>生成Json文本</strong>，调用generateJsonText可以把Json实例转换
 * 为对应的Json文本。重写方法toString方法返回Json实例对应的文本，等同于调用不
 * 带参数的方法generateJsonText。</p>
 * 
 * <p>方法<strong>isEmpty</strong>可以判断Json实例是否包含子元素，方法<strong>
 * count</strong>返回子元素的的个数，方法<strong>clear</strong>可以清除所有的
 * 子元素。getType方法Json实例的类型。</p>
 * 
 * <p创建、修改Json实例的过程中可能会产生Json实例间的<strong>循环引用</strong>，
 * 可以用方法existsCircle检测。</p>
 * 
 * @author bantouyan
 * @version 1.00
 */
public abstract class Json
{
    protected static Json nullJson = new JsonPrimitive();
    protected static Json trueJson = new JsonPrimitive(true);
    protected static Json falseJson = new JsonPrimitive(false);
    
    /**
     * 返回逻辑型的Json实例。
     * @param value 所需要的Json实例对应的逻辑型值
     * @return 对应的Json实例
     */
    protected static Json getBooleanJson(boolean value)
    {
        return (value == true)? trueJson: falseJson;
    }
    
    /**
     * 解析Json字符串为Json实例。
     * @param jsonText Json文本
     * @return 对应的Json实例
     * @throws IOException 发生了IO异常
     * @throws JsonParseException Json文本格式不正确
     */
    public static Json parseJsonText(String jsonText) throws IOException, JsonParseException
    {
        Json json = null;
        
        JsonTextParser jsonParser = new JsonTextParser(jsonText);
        json = jsonParser.parse();
        
        return json;
    }
    
    /**
     * 解析reader包含的Json文本流为Json实例。
     * @param reader 包含Json文本的Reader实例
     * @return 对应的Json实例
     * @throws IOException 读写reader发生异常
     * @throws JsonParseException reader所包含的Json文本格式不正确
     */
    public static Json parseJsonReader(Reader reader) throws IOException, JsonParseException
    {
        Json json = null;
        
        JsonTextParser jsonParser = new JsonTextParser(reader);
        json = jsonParser.parse();
        
        return json;
    }
    
    /**
     * 将Java Map实例解析为JsonObject实例。
     * @param map 要解析的Java Map实例
     * @return 对应的JsonObject实例
     * @throws JsonParseException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public static JsonObject parseJavaMap(Map<?, ?> map) throws JsonParseException
    {
        IdentityStack parentRef = new IdentityStack();
        return Json.parseJavaMap(map, parentRef);
    }
    
    /**
     * 将Java Map实例解析为JsonObject实例。
     * @param map 要解析的Java Map实例
     * @param parentRef 上级对象的堆栈，用于检测循环引用
     * @return 对应的JsonObject实例
     * @throws JsonParseException 如果Map元素已被（上级对象）引用，或有无法解析的对象，则抛出异常。
     */
    private static JsonObject parseJavaMap(Map<?, ?> map, IdentityStack parentRef)
    throws JsonParseException
    {
        if(parentRef.contains(map))
        {
            throw new JsonParseException("Circle reference exists in source Java instance.");
        }
        
        JsonObject json = new JsonObject();
        parentRef.push(map);
        Set<?> keys = map.keySet();
        for(Object key: keys)
        {
            if(!(key instanceof String || key instanceof Number || key instanceof Boolean))
            {
                throw new JsonParseException("Map key cannot cast to string.");
            }
            
            Object value = map.get(key);
            Json element = Json.changeToJson(value, parentRef);
            json.add(key.toString(), element);
        }
        parentRef.pop();
        return json;
    }
    
    /**
     * 将Java Collection实例解析为JsonArray实例。
     * @param collection 要解析的Java Collection实例
     * @return 对应的JsonArray实例
     * @throws JsonParseException 如果Collection内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public static JsonArray parseJavaCollection(Collection<?> collection) throws JsonParseException
    {
        IdentityStack parentRef = new IdentityStack();
        return Json.parseJavaCollection(collection, parentRef);
    }
    
    /**
     * 将Java Collection实例解析为JsonArray实例。
     * @param collection 要解析的Java Collection实例
     * @param parentRef 上级对象的堆栈，用于检测循环引用
     * @return 对应的JsonArray实例
     * @throws JsonParseException 如果Collection内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    private static JsonArray parseJavaCollection(Collection<?> collection, IdentityStack parentRef)
    throws JsonParseException
    {
        if(parentRef.contains(collection))
        {
            throw new JsonParseException("Circle reference exists in source Java instance.");
        }
        
        JsonArray json = new JsonArray();
        parentRef.push(collection);
        for(Object value: collection)
        {
            Json element = Json.changeToJson(value, parentRef);
            json.append(element);
        }
        parentRef.pop();
        return json;
    }
    
    /**
     * 将Java对象转换为Json实例
     * @param value Java对象
     * @return 对应的Json实例
     * @throws JsonParseException 如果Java对象已被（上级Json实例）引用，或无法解析，则抛出异常
     */
    private static Json changeToJson(Object value, IdentityStack parentRef) throws JsonParseException
    {
        Json json = null;
        
        if(value == null)
        {
            json = new JsonPrimitive();
        }
        else if(value instanceof String)
        {
            json = new JsonPrimitive((String)value);
        }
        else if(value instanceof Number)
        {
            json = new JsonPrimitive((Number)value);
        }
        else if(value instanceof Boolean)
        {
            json = new JsonPrimitive((Boolean)value);
        }
        else if(value instanceof Map<?, ?>)
        {
            json = Json.parseJavaMap((Map<?, ?>)value, parentRef);
        }
        else if(value instanceof Collection<?>)
        {
            json = Json.parseJavaCollection((Collection<?>)value, parentRef);
        }
        else if(value instanceof Jsonable)
        {
            json = ((Jsonable)value).generateJson();
        }
        else
        {
            throw new JsonParseException("Cannot parse value: " + value + ".");
        }
        
        return json;
    }

    /**
     * 生成标准的Json文本， 如果解析失败，则返回空字符串。
     */ 
    public final String generateJsonText()
    {
        String str = null;
        try
        {
            str = generateJsonText(true);
        } 
        catch (JsonException e)
        {
            str = "";
        }
        
        return str;
    }
    
    /**
     * 生成对应的Json文本
     * @param useQuote true Object的Name部分加引号， false尽量不加引号
     * @return 对应的Json文本
     * @throws JsonException JsonException 如果Json实例内出现了循环引用，则抛出此异常
     */
    public final String generateJsonText(boolean useQuote)throws JsonException
    {
        if(existsCircle())
        {
            throw new JsonException("Circle reference occured in this Json.");
        }
        
        StringBuilder builder = new StringBuilder();
        generateJsonText(builder, useQuote);
        return builder.toString();
    }

    /**
     * 生成Json文本，并追加到参数builder的尾部
     * @param builder 保存Json文本的StringBuilder
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     */
    protected abstract void generateJsonText(StringBuilder builder, boolean useQuote);
    
    /**
     * 判断Json对象内是否存在循环引用。
     * @return 有循环引用返回true，否则返回false
     */
    public final boolean existsCircle()
    {
        IdentityStack parentRef = new IdentityStack();
        boolean rst = existsCircle(parentRef);
        
        return rst;
    }
    
    /**
     * 判断Json实例内是否存在循环引用。
     * @param parentRef 上级Json对象堆栈，用于检测循环引用
     * @return 有循环引用返回true，否则返回false
     */
    protected abstract boolean existsCircle(IdentityStack parentRef);
    
    /**
     * 清空Json实例所有的成员或元素，对JsonPrimitive无意义。
     */
    public abstract void clear();
    
    /**
     * 判断Json实例是否不含有任何子元素，JsonPrimitive返回false。
     * @return 不包含任何子元素返回true，否则返回false
     */
    public abstract boolean isEmpty();
    
    /**
     * 返回Json实例子元素的数目， JsonPrimitive返回0。
     * @return Json实例子元素的数目
     */
    public abstract int count();
    
    /**
     * 返回Json实例的类型。
     * @return 对应的Json类型
     */
    public abstract JsonType getType();
    
    /**
     * 转换为标准的Json字符串。
     */
    @Override
    public final String toString()
    {
        return generateJsonText();
    }
    
    /**
     * 判断是否相等，如果都是Json实例，类型相同，子元素的个数相同，
     * 且对应的子元素相等（对JsonArray来讲，下标相同的子元素相等，
     * 对JsonObject来讲，Name相同的子元素相等），则判断为相等，否则不相等。
     * @param obj 要比较的对象
     * @return 相等返回true，否则返回false
     */
    @Override
    public final boolean equals(Object obj)
    {
        if(obj == null)
        {
            return false;
        }
        else if(this == obj)
        {
            return true;
        }
        else if(obj instanceof Json)
        {
            Json objJson = (Json)obj;
            JsonType jtp = this.getType();
            if(jtp != objJson.getType())
            {
                return false;
            }
            else if(jtp == JsonType.NULL)
            {
                return true;
            }
            else if(this.count() != objJson.count())
            {
                return false;
            }
            else
            {
                return this.same(objJson);
            }
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 判断两个Json实例表示的数据是否一致。
     * @param obj 被比较的Json实例
     * @return 一致返回true，不一致返回false
     */
    protected abstract boolean same(Json obj);
    
    /**
     * Json实例的hash值，不同的子类型有不同的计算方法
     * @return hash值
     */
    @Override
    public abstract int hashCode();
    
    /**
     * Json实例的类型。
     * OBJECT表示Json对象；
     * ARRAY表示Json数组；
     * STRING表示Json字符串；
     * INTEGER表示Json整型数值，用Long类型存储；
     * FLOAT表示Json浮点型数值，用Double类型存储；
     * BOOLEAN表示Json逻辑型，用Boolean类型存储；
     * NULL表示Json null类型。
     * 
     * @author bantouyan
     * @version 0.1
     */
    public static enum JsonType
    {
        /**
         * 表示Json对象类型
         */
        OBJECT, 
        
        /**
         * 表示Json数组类型
         */
        ARRAY, 
        
        /**
         * 表示Json字符串类型
         */
        STRING, 
        
        /**
         * 表示Json数值（整型，用Long存储）类型
         */
        INTEGER, 
        
        /**
         * 表示Json数值（浮点型，用Double存储）类型
         */
        FLOAT, 
        
        /**
         * 表示Json逻辑型（布尔型）数据
         */
        BOOLEAN, 
        
        /**
         * 表示Json null类型
         */
        NULL
    };
}
