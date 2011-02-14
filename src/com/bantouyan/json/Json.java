package com.bantouyan.json;

import java.io.IOException;
import java.util.HashSet;

/**
 * 所有Json类的超类，用来提供操作Json的通用接口。
 * @author bantouyan
 * @version 0.1
 */
public abstract class Json
{
    protected static Json nullJson = new JsonPrimitive();
    protected static Json trueJson = new JsonPrimitive(true);
    protected static Json falseJson = new JsonPrimitive(false);
    
    /**
     * 返回逻辑型的Json对象
     * @param value
     * @return
     */
    protected static Json getBooleanJson(boolean value)
    {
        return (value == true)? trueJson: falseJson;
    }
    
    /**
     * 解析Json字符串为Json对象。
     * @param jsonText
     * @return
     * @throws IOException
     * @throws JsonException
     */
    public static Json parseJsonText(String jsonText) throws IOException, JsonException
    {
        Json json = null;
        
        JsonTextParser jsonParser = new JsonTextParser(jsonText);
        json = jsonParser.parse();
        
        return json;
    }
    
    /**
     * 生成Json文本。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @param useStandard
     * @return
     * @throws JsonException 如果JsonArray与JsonObject出现了循环引用，则抛出此异常
     */
    public abstract String generateJsonText(boolean useStandard)throws JsonException;
    

    /**
     * 生成Json文本。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @param useStandard
     * @return
     * @throws JsonException  如果JsonArray与JsonObject出现了循环引用，则抛出此异常
     */
    protected abstract String generateJsonText(boolean useStandard, HashSet<Json> parentRef) throws JsonException;
    
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
     * 清空Json对象所有的成员或元素，对JsonPrimitive无意义。
     */
    public abstract void clear();
    
    /**
     * 判断Json对象是否含有成员或元素，JsonPrimitive返回false。
     * @return
     */
    public abstract boolean isEmpty();
    
    /**
     * 返回Json对象成员或元素的数目， JsonPrimitive返回1。
     * @return
     */
    public abstract int count();
    
    /**
     * 返回Json对象的类型。
     * @return
     */
    public abstract JsonType getType();
    
    /**
     * 如果是STRING类型，返回Json对象的原始字符串值，否则返回对应的Json文本。
     * @return
     */
    public abstract String getString();
    
    /**
     * 返回Json对象的整型值。
     * @return
     * @throws JsonException
     */
    public abstract long getLong() throws JsonException;
    
    /**
     * 返回Json对象的浮点型值。
     * @return
     * @throws JsonException
     */
    public abstract double getDouble() throws JsonException;
    
    /** 
     * 返回Json对象的逻辑型（布尔型）值。
     * @return
     * @throws JsonException
     */
    public abstract boolean getBoolean() throws JsonException;
    
    /**
     * 转换为标准的Json字符串。
     */
    @Override
    public final String toString()
    {
        return generateJsonText();
    }
    
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
    
    /*
     * Json对象的类型。
     */
    public static enum JsonType{OBJECT, ARRAY, STRING, INTEGER, FLOAT, BOOLEAN, NULL};
    
    public static void main(String[] args) throws IOException, JsonException
    {
        JsonObject jsonObj = (JsonObject)Json.parseJsonText("{a:\'A\', \'b\':\'B\'}");
        JsonArray jsonAry = (JsonArray)Json.parseJsonText("[1, 2, 3]");
        jsonAry.append(jsonObj);
//       jsonObj.add("root", jsonAry);
        
        String str = jsonAry.generateJsonText();
        
        System.out.println(str);
    }

}
