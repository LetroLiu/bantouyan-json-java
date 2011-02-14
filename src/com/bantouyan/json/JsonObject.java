package com.bantouyan.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 用来表示Json Object对象。
 * @author bantouyan
 * @version 0.1
 */
public class JsonObject extends Json
{  
    //不允许出现key为null的entry，但允许value为null
    //值为null的value，get以及转换为Json文本时当类型为NULL的Json对象处理
    private HashMap<String, Json> data = null;
    
    public JsonObject()
    {
        this.data = new HashMap<String, Json>();
    }
    
    public JsonObject(HashMap<String, Json> data)
    {
        if(data.containsKey(null)) data.remove(null);
        
        this.data = data;
    }
    
    /**
     * 返回指定name的Json对象，如果获取的Json对象是null，则返回type是NULL类型的Json对象。
     * @param name
     * @return 如果name为null，或不包含此name，则返回null
     */
    public Json get(String name)
    {
        if(name == null || ! this.data.containsKey(name)) return null;
        
        Json json = this.data.get(name);
        if(json == null)
        {
            json = Json.nullJson;
            this.data.put(name, json);
        }
        
        return json;
    }
    
    /**
     * 返回指定name的Json对象的字符串值，如果获取的Json对象是null，则按NULL类型返回。
     * @param name
     * @return 如果name为null，或不包含此name，则返回null
     */
    public String getString(String name)
    {
        Json json = this.get(name);
        
        return (json == null)? null: json.toString();
    }
    
    /**
     * 返回指定那么的Json对象的逻辑型（布尔型）值
     * @param name
     * @return 如果name为null，或不包含此name，则返回null
     * @throws JsonException 如果name为null，或不存在此name，或类型bu匹配，则抛出异常
     */
    public boolean getBoolean(String name) throws JsonException
    {
        Json json = this.get(name);
        
        if(json == null)
        {
            throw new JsonException("Parameter name is null or this object not contain such name");
        }
        else 
        {
            return json.getBoolean();
        }
    }
    
    /**
     * 返回指定那么的Json对象的整型值
     * @param name
     * @return 如果name为null，或不包含此name，则返回null
     * @throws JsonException 如果name为null，或不存在此name，或类型bu匹配，则抛出异常
     */
    public long getLong(String name) throws JsonException
    {
        Json json = this.get(name);
        
        if(json == null)
        {
            throw new JsonException("Parameter name is null or this object not contain such name");
        }
        else
        {
            return json.getLong();
        }
    }
    
    /**
     * 返回指定那么的Json对象的浮点型值
     * @param name
     * @return 如果name为null，或不包含此name，则返回null
     * @throws JsonException 如果name为null，或不存在此name，或类型bu匹配，则抛出异常
     */
    public double getDouble(String name) throws JsonException
    {
        Json json = this.get(name);
        
        if(json == null)
        {
            throw new JsonException("Parameter name is null or this object not contain such name");
        }
        else 
        {
            return json.getDouble();
        }
    }
    
    /**
     * 添加一个新的成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name, Json value) throws JsonException
    {
        if(name == null) return null;
        
        if(this.data.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        return this.data.put(name, value);
    }

    /**
     * 添加一个新的成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name, Jsonable value) throws JsonException
    {
        Json json = (value == null)? Json.nullJson: value.generateJson();
        return add(name, json);
    }

    /**
     * 添加一个新的成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name, String value) throws JsonException
    {
        Json json = (value == null)? Json.nullJson: new JsonPrimitive(value);
        return add(name, json);
    }

    /**
     * 添加一个新的成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name, long value) throws JsonException
    {
        return add(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个新的成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name, double value) throws JsonException
    {
        return add(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个新的成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name, boolean value) throws JsonException
    {
        return add(name, Json.getBooleanJson(value));
    }

    /**
     * 添加一个新的NULL类型的Json成员, 如果Name已经存在，则抛出异常。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return null
     * @throws JsonException 
     */
    public Json add(String name) throws JsonException
    {
        return add(name, Json.nullJson);
    }
    
    /**
     * 批量添加成员,但忽略name为null的entry。
     * @param map
     */
    public void addAll(Map<String, ? extends Json> map) throws JsonException
    {
        Set<String> keys = map.keySet();
        ArrayList<String> conflictNames = new ArrayList<String>();
        for(String name: keys)
        {
            if(this.data.containsKey(name))
            {
                conflictNames.add(name);
            }
        }
        if(conflictNames.isEmpty())
        {
            if(map.containsKey(null)) map.remove(null);
            this.data.putAll(map);
        }
        else
        {
            String msg = "Names \"" + conflictNames.get(0);
            for(int i=1; i<conflictNames.size(); i++)
            {
                msg += "\", \"" + conflictNames.get(i);
            }
            msg += "\" already exist in this JsonObject";
            throw new JsonException(msg);
        }
    }

    /**
     * 批量添加成员，但忽略name为null的entry。
     * @param map
     * @throws JsonException 
     */
    public void addAllJsonable(Map<String, ? extends Jsonable> map) throws JsonException
    {
        for(Map.Entry<String, ? extends Jsonable> entry: map.entrySet())
        {
            add(entry.getKey(), entry.getValue().generateJson());
        }
    }
    
    /**
     * 设置指定Name的成员， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name, Json value)
    {
        if(name == null) return null;
        if(value == null) value = Json.nullJson;
        return this.data.put(name, value);
    }

    /**
     * 设置指定Name的成员， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name, Jsonable value)
    {
        if(name == null) return null;
        if(value == null)
            return this.data.put(name, Json.nullJson);
        else
            return this.data.put(name, value.generateJson());
    }

    /**
     * 设置指定Name的成员， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name, String value)
    {
        if(name == null) return null;
        Json json = (value == null)? Json.nullJson: new JsonPrimitive(value);
        return this.data.put(name, json);
    }

    /**
     * 设置指定Name的成员， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name, long value)
    {
        if(name == null) return null;
        return this.data.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的成员， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name, double value)
    {
        if(name == null) return null;
        return this.data.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的成员， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @param value
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name, boolean value)
    {
        if(name == null) return null;
        return this.data.put(name, Json.getBooleanJson(value));
    }

    /**
     * 设置指定Name的成员为NULL类型的Json对象， 如果不存在相应的Name，则创建新的成员。
     * @param name 如果name为null，则不执行任何操作
     * @return 如果name不为null，则返回先前与name对应的value，否则null
     */
    public Json set(String name)
    {
        if(name == null) return null;
        return this.data.put(name, Json.nullJson);
    }

    /**
     * 批量设置成员，但忽略name为null的entry。
     * @param name
     * @param map
     */
    public void setAll(Map<String, ? extends Json> map)
    {
        if(map.containsKey(null)) map.remove(null);
        this.data.putAll(map);
    }

    /**
     * 批量设置成员，但忽略name为null的entry。
     * @param name
     * @param map
     */
    public void setAllJsonable(Map<String, ? extends Jsonable> map)
    { 
        if(map.containsKey(null)) map.remove(null);
        for(Map.Entry<String, ? extends Jsonable> entry: map.entrySet())
        {
            this.data.put(entry.getKey(), entry.getValue().generateJson());
        }
    }
    
    /**
     * 删除指定Name的成员。
     * @param name
     */
    public void remove(String name)
    {
        this.data.remove(name);
    }
    
    /**
     * 判断是否存在同名的name。
     * @param name
     * @return
     */
    public boolean containsName(String name)
    { 
        return this.data.containsKey(name);
    }
    
    /**
     * 返回Json对象所有成员的Name。
     * @return
     */
    public Set<String> nameSet()
    {
        return this.data.keySet();
    }
    
    /**
     * 清除所有的成员。
     */
    @Override
    public void clear()
    {
        this.data.clear();
    }
    
    /**
     * 判断Json对象是否不含任何成员。
     * @return
     */
    @Override
    public boolean isEmpty()
    {
        return this.data.isEmpty();
    }
    
    /**
     * 返回所有的成员的数量。
     * @return
     */
    @Override
    public int count()
    {
        return this.data.size();
    }
    
    /**
     * 生成Json文本。
     * @param useStandard true生成标准文本，false则尝试在name部分不加引号
     * @return
     * @throws JsonException
     */
    @Override 
    public String generateJsonText(boolean useStandard) throws JsonException
    {
        HashSet<Json> parentRef = new HashSet<Json>();        
        return generateJsonText(useStandard, parentRef);
    }
    
    /**
     * 生成Json文本。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @param parentRef parent to root Json reference, used to check error
     * @return
     */
    @Override
    protected String generateJsonText(boolean useStandard, HashSet<Json> parentRef)
            throws JsonException
    {
        if(parentRef.contains(this))
        {
            throw new JsonException("Circle reference occured in this Json.");
        }        
        parentRef.add(this);
        
        StringBuilder build = new StringBuilder();
        int cnt = data.size();
        int cur = 0;
        
        build.append('{');
        for(Entry<String, Json> entry: data.entrySet())
        {
            String name = entry.getKey();
            if(useStandard)
            {
                build.append(JsonTextParser.toJsonString(name));
            }
            else
            {
                build.append(JsonTextParser.toJsonNoquoteString(name));
            }
            build.append(':');
            Json value = entry.getValue();
            if(value == null)
            {
                build.append("null");
            }
            else
            {
                build.append(value.generateJsonText(useStandard, parentRef));
            }
            
            cur++;
            if(cur != cnt) build.append(',');
        }
        build.append('}');
        
        parentRef.remove(this);
        return build.toString();
    }

    /**
     * 返回 Json对象类型 JsonType.OBJECT。
     */
    @Override
    public JsonType getType()
    {
        return JsonType.OBJECT;
    }

    /**
     * 返回Json 对象的字符串值。
     * @return
     */
    @Override
    public String getString()
    {
        return this.toString();
    }

    /**
     * 返回Json对象的整型值。
     * @return
     * @throws JsonException
     */
    @Override
    public long getLong() throws JsonException
    {
        throw new JsonException("Cannot transfer JsonObject to long value.");
    }

    /**
     * 返回Json对象的浮点型值。
     * @return
     * @throws JsonException
     */
    @Override
    public double getDouble() throws JsonException
    {
        throw new JsonException("Cannot transfer JsonObject to double value.");
    }

    /**
     * 返回Json对象的逻辑型（布尔型）值。
     * @return
     * @throws JsonException
     */
    @Override
    public boolean getBoolean() throws JsonException
    {
        throw new JsonException("Cannot transfer JsonObject to boolean value.");
    }
}
