package com.bantouyan.json;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 所有Json实例的超类，用来提供操作Json的通用接口。
 * @author bantouyan
 * @version 0.1
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
     * @throws JsonException Json文本格式不正确
     */
    public static Json parseJsonText(String jsonText) throws IOException, JsonException
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
     * @throws JsonException reader所包含的Json文本格式不正确
     */
    public static Json parseJsonReader(Reader reader) throws IOException, JsonException
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
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public static JsonObject parseJavaMap(Map<?, ?> map) throws JsonException
    {
        IdentityStack parentRef = new IdentityStack();
        return Json.parseJavaMap(map, parentRef);
    }
    
    /**
     * 将Java Map实例解析为JsonObject实例。
     * @param map 要解析的Java Map实例
     * @param parentRef 上级对象的堆栈，用于检测循环引用
     * @return 对应的JsonObject实例
     * @throws JsonException 如果Map元素已被（上级对象）引用，或有无法解析的对象，则抛出异常。
     */
    private static JsonObject parseJavaMap(Map<?, ?> map, IdentityStack parentRef)
    throws JsonException
    {
        if(parentRef.contains(map))
        {
            throw new JsonException("Circle reference exists in source Java instance.");
        }
        
        JsonObject json = new JsonObject();
        parentRef.push(map);
        Set<?> keys = map.keySet();
        for(Object key: keys)
        {
            if(!(key instanceof String || key instanceof Number || key instanceof Boolean))
            {
                throw new JsonException("Map key cannot cast to string.");
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
     * @throws JsonException 如果Collection内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public static JsonArray parseJavaCollection(Collection<?> collection) throws JsonException
    {
        IdentityStack parentRef = new IdentityStack();
        return Json.parseJavaCollection(collection, parentRef);
    }
    
    /**
     * 将Java Collection实例解析为JsonArray实例。
     * @param collection 要解析的Java Collection实例
     * @param parentRef 上级对象的堆栈，用于检测循环引用
     * @return 对应的JsonArray实例
     * @throws JsonException 如果Collection内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    private static JsonArray parseJavaCollection(Collection<?> collection, IdentityStack parentRef)
    throws JsonException
    {
        if(parentRef.contains(collection))
        {
            throw new JsonException("Circle reference exists in source Java instance.");
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
     * @param value
     * @return
     * @throws JsonException 如果Java对象已被（上级Json实例）引用，或无法解析，则抛出异常
     * @throws EOFException 
     */
    private static Json changeToJson(Object value, IdentityStack parentRef) throws JsonException
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
            throw new JsonException("Cannot parse value: " + value + ".");
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
     * 生成Json文本。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @return 对应的Json文本
     * @throws JsonException 如果Json实例内出现了循环引用，则抛出此异常
     */
    public final String generateJsonText(boolean useStandard)throws JsonException
    {
        if(existsCircle())
        {
            throw new JsonException("Circle reference occured in this Json.");
        }    
        return generateJsonTextWithoutCheck(useStandard);
    }

    /**
     * 生成Json文本，但不检测循环引用。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @return 对应的Json文本
     */
    protected abstract String generateJsonTextWithoutCheck(boolean useStandard);
    
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
     * 判断Json实例是否不含有任何成员或元素，JsonPrimitive返回false。
     * @return
     */
    public abstract boolean isEmpty();
    
    /**
     * 返回Json实例子元素的数目， JsonPrimitive返回0。
     * @return
     */
    public abstract int count();
    
    /**
     * 返回Json实例的类型。
     * @return 对应的Json类型
     */
    public abstract JsonType getType();
    
    /**
     * 返回Json实例对应的字符串。
     * @return Primitive实例返回不带引号的字符串，其他实例返回标准Json文本
     */
    public abstract String getString();
    
    /**
     * 转换为标准的Json字符串。
     */
    @Override
    public final String toString()
    {
        return generateJsonText();
    }
    
    /**
     * 判断是否相等。
     * @param obj 要比较的对象
     * @return 如果都是Json实例，且对应的标准Json文本相等，则返回true，否则返回false
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
            String thisStr = null;
            String objStr = null;
            
            try
            {
                thisStr = this.generateJsonText(true);
                objStr = ((Json) obj).generateJsonText(true);
            } 
            catch (JsonException e)
            {
                return false;
            }
            
            return thisStr.equals(objStr);
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Json实例的hash值
     * @return 根据对应的标准Json文本生成hash值
     */
    @Override
    public final int hashCode()
    {
        try
        {
            return this.generateJsonText(true).hashCode();
        } 
        catch (JsonException e)
        {
            return 0;
        }
    }
    
    /**
     * Json实例的类型。
     * OBJECT Json对象
     * ARRAY Json数组
     * STRING Json字符串
     * INTEGER Json整型数值，用Long类型存储
     * FLOAT Json浮点型数值，用Double类型存储
     * BOOLEAN Json逻辑型，用Boolean类型存储
     * NULL Json null类型
     */
    public static enum JsonType{OBJECT, ARRAY, STRING, INTEGER, FLOAT, BOOLEAN, NULL};
    
    public static void main(String[] args) throws Exception
    {
    }

}
