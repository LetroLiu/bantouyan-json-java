package com.bantouyan.json;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>所有Json实例的抽象超类，用来提供操作Json的通用接口。</p>
 * 
 * <p><strong>生成Json实例</strong>，把文本转换为Json实例可以调用类方法
 * <strong>parseJsonText</strong>或<strong>parseJsonReader</strong>，
 * 如果想从Java集合生成Json实例，则可以调用类方法<strong>parseJavaMap</strong>
 * 或<strong>parseJavaCollection</strong>。</p>
 * 
 * <p><strong>生成Json文本</strong>，调用方法<strong>generateJsonText</strong>
 * 可以把Json实例转换为对应的Json文本。重写的方法<strong>toString</strong>
 * 返回Json实例对应的文本，等同于调用不带参数的方法<strong>generateJsonText</strong>。
 * 如果想把Json文本输出到字符流，请调用方法<strong>outputToWriter</strong>。
 * </p>
 * 
 * <p>方法<strong>isEmpty</strong>可以判断Json实例子元素的个数是否为零，
 * 方法<strong>count</strong>返回子元素的的个数，
 * 方法<strong>clear</strong>可以清除所有的子元素。
 * 方法<strong>getType</strong>返回Json实例的类型。</p>
 * 
 * <p>创建、修改Json实例的过程中可能会产生Json实例间的<strong>循环引用</strong>，
 * 可以用方法<strong>existsCircle</strong>检测。</p>
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public abstract class Json
{
    /**
     * 类型为NULL的Json实例。
     */
    protected final static JsonPrimitive nullJson = new JsonPrimitive();
    
    /**
     * 类型为BOOLEAN值为true的Json实例。
     */
    protected final static JsonPrimitive trueJson = new JsonPrimitive(true);
    
    /**
     * 类型为BOOLEAN值为false的Json实例。
     */
    protected final static JsonPrimitive falseJson = new JsonPrimitive(false);
    
    /**
     * 返回值为true或false的逻辑型的Json实例。
     * @param value true或false
     * @return 对应的Json实例
     */
    protected static JsonPrimitive getBooleanJson(boolean value)
    {
        return (value == true)? trueJson: falseJson;
    }
    
    /**
     * 解析Json字符串为Json实例。
     * @param jsonText Json文本，应该为一个完整的JsonArray或JsonObject的表示。
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
     * @param reader 包含Json文本的Reader实例，整个字符流应该是一个完整的JsonArray或JsonObject的表示
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
     * 将Java Map实例解析为JsonObject实例，但忽略key为null的entry。
     * @param map 要解析的Java Map实例
     * @return 对应的JsonObject实例
     * @throws JsonParseException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public static JsonObject parseJavaMap(Map<?, ?> map) throws JsonParseException
    {
        map.remove(null);
        IdentityStack parentRef = new IdentityStack();
        return Json.parseJavaMap(map, parentRef);
    }
    
    /**
     * 将Java Map实例解析为JsonObject实例，但忽略key为null的entry。
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
            throw new JsonParseException("Java map is referenced again.");
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
     * 将Java Collection实例解析为JsonArray实例，但忽略子Map内key为null的entry。
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
     * 将Java Collection实例解析为JsonArray实例，但忽略子Map内key为null的entry。
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
            throw new JsonParseException("Java colloection is referenced again.");
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
     * 将Java对象转换为Json实例，但忽略Map内key为null的entry。
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
        else if(value instanceof Json)
        {
            json = (Json)value;
        }
        else if(value instanceof Jsonable)
        {
            json = ((Jsonable)value).generateJson();
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
        else
        {
            throw new JsonParseException("Cannot parse value: " + value + " to json instance.");
        }
        
        return json;
    }

    /**
     * 生成标准的Json文本， 如果解析失败，则返空指针。
     * @return 对应的Json文本
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
            str = null;
        }
        
        return str;
    }
    
    /**
     * 生成对应的Json文本。
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     * @return 对应的Json文本
     * @throws JsonException JsonException 如果Json实例内出现了循环引用，则抛出此异常
     */
    public final String generateJsonText(boolean useQuote)throws JsonException
    {
        if(existsCircle())
        {
            throw new JsonException("Circle reference exists in this Json.");
        }
        
        StringBuilder builder = new StringBuilder();
        try
        {
            appendToAppendable(builder, useQuote);
        }
        catch (IOException e)
        {
            //这里的IOException是由于appendToAppendable中调用jsonStringToAppendable(String, StringBuilder)
            //和jsonStringToAppendableWithoutQuote(String, StringBuilder)引起的，
            //这两个方法只会调用StringBuild.append(char)和StringBuilder.append(String)，
            //所以不会产生异常，故不用处理
        }
        return builder.toString();
    }
    
    /**
     * 将Json对应的文本输出到字符流，不用处理IO异常，适合在Servlet内使用。
     * @param writer 接受Json文本的字符流
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     * @throws JsonException Json内存在循环引用
     */
    public final void outputToWriter(PrintWriter writer, boolean useQuote) throws JsonException
    {
        if(existsCircle())
        {
            throw new JsonException("Circle reference exists in this Json.");
        }
        try
        {
            appendToAppendable(writer, useQuote);
        } 
        catch (IOException e)
        {
            //这里的IOException是由于appendToAppendable中调用
            //jsonStringToAppendable(String, PrintWriter)
            //引起的，该方法只会调用PrintWriter.append(char)
            //和PrintWriter.append(String)，不会产生异常，
            //故不用处理。
        }
    }
    
    /**
     * 将Json对应的文本输出到字符流。
     * @param writer 接受Json文本的字符流
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     * @throws IOException IO异常
     * @throws JsonException Json内存在循环引用
     */
    public final void outputToWriter(Writer writer, boolean useQuote) throws IOException, JsonException
    {
        if(existsCircle())
        {
            throw new JsonException("Circle reference exists in this Json.");
        }
        appendToAppendable(writer, useQuote);
    }
    
    /**
     * 向可追加对象追加Json文本。
     * @param dest 接受Json文本的可追加对象
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     * @throws IOException 追加字符流发生IO异常
     */
    protected abstract void appendToAppendable(Appendable dest, boolean useQuote) throws IOException; 
    
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
     * 清空Json实例所有的子元素，对JsonPrimitive无意义。
     */
    public abstract void clear();
    
    /**
     * 判断Json实例子元素的个数是否为零，JsonPrimitive返回true。
     * @return 子元素的个数为零返回true，否则返回false
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
     * 转换为标准的Json字符串，如果转换失败则返回null。
     * @return 对应的Json字符串
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
     * @exception JsonException 参与比较的Json实例内存在循环引用
     */
    @Override
    public final boolean equals(Object obj) throws JsonException
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
                if(this.existsCircle())
                {
                    throw new JsonException("The json instance used to compare exists circle.");
                }
                else if(objJson.existsCircle())
                {
                    throw new JsonException("The json instance used to be compare exists circle.");
                }
                else
                {
                    return this.same(objJson);
                }
            }
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 判断两个Json实例表示的数据是否一致，即是否表示相同的Json。
     * @param obj 被比较的Json实例
     * @return 一致返回true，不一致返回false
     */
    protected abstract boolean same(Json obj);
    
    /**
     * Json实例的hash值，不同的子类型有不同的计算方法。
     * @return hash值
     */
    @Override
    public abstract int hashCode();
    
    /**
     * Json实例的类型。</br>
     * <strong>OBJECT</strong>表示Json对象，用类型JsonArray存储；</br>
     * <strong>ARRAY</strong>表示Json数组，用类型JsonArray存储；</br>
     * <strong>STRING</strong>表示Json字符串，用类型String存储；</br>
     * <strong>INTEGER</strong>表示Json整型数值，用类型Long存储；</br>
     * <strong>FLOAT</strong>表示Json浮点型数值，用类型Double存储；</br>
     * <strong>BOOLEAN</strong>表示Json逻辑型，用类型Boolean存储；</br>
     * <strong>NULL</strong>表示Json null类型，用类型String存储。
     * 
     * @author 飞翔的河马
     * @version 1.00
     * 
     */
    public static enum JsonType
    {
        /**
         * 表示Json对象类型（用JsonObject存储）。
         */
        OBJECT, 
        
        /**
         * 表示Json数组类型（用JsonArray存储）。
         */
        ARRAY, 
        
        /**
         * 表示Json字符串类型（用String存储）。
         */
        STRING, 
        
        /**
         * 表示Json数值（整型，用Long存储）类型。
         */
        INTEGER, 
        
        /**
         * 表示Json数值（浮点型，用Double存储）类型。
         */
        FLOAT, 
        
        /**
         * 表示Json逻辑型（布尔型，用Boolean存储）数据，只有两个值，true和false。
         */
        BOOLEAN, 
        
        /**
         * 表示Json null类型（用String存储），只有一个值null。
         */
        NULL
    };
}
