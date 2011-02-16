package com.bantouyan.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>用来表示Json对象实例。Json对象是由Name Value对的无序集合构成（本文档中
 * Name Value对中的Value部分称为子元素），可以通过Name存取对应的Value。</p>
 * 
 * <p><strong>创建JsonObject实例</strong>，除了可以通过调用Json类的parse开头
 * 的方法创建JsonObject实例外，还可以直接创建空的JsonArray实例，或从Json实例集合创建。</p>
 * 
 * <p>调用方法get可以<strong>获取</strong>指定Name的子元素，方法<strong>getXXX
 * </strong>返回指定Name子元素的某种原始类型值，方法<strong>canToXXX</strong>
 * 判定指定Name的子元素是否可以转换为这种原始类型，<strong>重设</strong>
 * 指定Name的子元素调用方法set，<strong>批量重设</strong>调用方法setAll，
 * <strong>添加</strong>Name Value（子元素）对调用方法add，<strong>批量添加
 * </strong>调用方法addAll，<strong>删除</strong>指定Name的子元素调用方法remove。
 * （add与set的区别是如果指定Name的子元素存在，或Name为null，则add方法抛出异常，
 * 但set方法不会）</p>
 * 
 * <p>方法<strong>isEmpty</strong>可以判断JsonObject实例是否包含
 * 子元素，方法<strong>count</strong>返回子元素的的个数（即Name Value对的个数），
 * 方法<strong>clear</strong>可以清除所有的子元素（Name Value对）。方法<strong>
 * getType</strong>返回JsonObject实例的类型JsonType.OBJECT。</p>
 * 
 * @author bantouyan
 * @version 1.00
 */
public class JsonObject extends Json
{  
    //不允许出现key为null的entry，但允许value为null
    //值为null的value，get以及转换为Json文本时当类型为NULL的Json实例处理
    private HashMap<String, Json> data = null;
    
    /**
     * 创建空的JsonObject实例。
     */
    public JsonObject()
    {
        this.data = new HashMap<String, Json>();
    }
    
    /**
     * 根据已有的Map创建JsonObject实例。
     * @param map 创建JsonObject的源数据，但会去掉map中key为null的key vlaue对
     */
    public JsonObject(Map<String, ? extends Json> map)
    {
        this.data = new HashMap<String, Json>();
        
        if(map.containsKey(null)) map.remove(null);
        this.data.putAll(map);
    }
    
    /**
     * 返回指定Name的子元素，如果子元素是null，则返回type是NULL类型的Json实例。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null，否则返回对应的子元素
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
     * 返回指定Name的子元素字符串值，如果获取的子元素是null，则按NULL类型返回。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null
     */
    public String getString(String name)
    {
        Json json = this.get(name);
        
        return (json == null)? null: json.toString();
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为逻辑型（布尔型）值。
     * @param name 子元素的Name
     * @return 可以返回true，否则返回false
     */
    public boolean canToBoolean(String name)
    {
        Json json = get(name);
        if(json == null)
        {
            return false;
        }
        else if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).canToBoolean();
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回指定Name的子元素的逻辑型（布尔型）值。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public boolean getBoolean(String name) throws JsonException
    {
        if(canToBoolean(name))
        {
            return ((JsonPrimitive)get(name)).getBoolean();
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to boolean value.");
        }
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为整型值
     * @param name 子元素的Name
     * @return 可以返回true，否则返回false
     */
    public boolean canToLong(String name)
    {
        Json json = get(name);
        if(json == null)
        {
            return false;
        }
        else if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).canToLong();
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回指定Name的子元素的整型值。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public long getLong(String name) throws JsonException
    {
        if(canToBoolean(name))
        {
            return ((JsonPrimitive)get(name)).getLong();
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to long value.");
        }
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为浮点型值。
     * @param name 子元素的Name
     * @return 可以返回true，否则返回false
     */
    public boolean canToDouble(String name)
    {
        Json json = get(name);
        if(json == null)
        {
            return false;
        }
        else if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).canToDouble();
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回指定Name的子元素的浮点型值。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public double getDouble(String name) throws JsonException
    {
        if(canToBoolean(name))
        {
            return ((JsonPrimitive)get(name)).getDouble();
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to double value.");
        }
    }
    
    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, Json value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.data.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        this.data.put(name, value);
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, Jsonable value) throws JsonException
    {
        Json json = (value == null)? Json.nullJson: value.generateJson();
        add(name, json);
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, String value) throws JsonException
    {
        Json json = (value == null)? Json.nullJson: new JsonPrimitive(value);
        add(name, json);
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, long value) throws JsonException
    {
        add(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, double value) throws JsonException
    {
        add(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, boolean value) throws JsonException
    {
        add(name, Json.getBooleanJson(value));
    }

    /**
     * 添加一个指定Name的新子元素，子元素是NULL类型实例。
     * @param name 子元素的Name
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name) throws JsonException
    {
        add(name, Json.nullJson);
    }
    
    /**
     * 批量添加子元素,但忽略name为null的entry。
     * @param map 要添加的子元素Map
     * @throws JsonException， 如果有同名子元素被加入，则抛出异常
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
     * 批量添加子元素,但忽略name为null的entry。
     * @param map 要添加的子元素Map
     * @throws JsonException， 如果有同名子元素被加入，则抛出异常
     */
    public void addAllJsonable(Map<String, ? extends Jsonable> map) throws JsonException
    {
        for(Map.Entry<String, ? extends Jsonable> entry: map.entrySet())
        {
            add(entry.getKey(), entry.getValue().generateJson());
        }
    }
    
    /**
     * 设置指定Name的子元素， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
     */
    public Json set(String name, Json value)
    {
        if(name == null) return null;
        if(value == null) value = Json.nullJson;
        return this.data.put(name, value);
    }

    /**
     * 设置指定Name的子元素， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
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
     * 设置指定Name的子元素， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
     */
    public Json set(String name, String value)
    {
        if(name == null) return null;
        Json json = (value == null)? Json.nullJson: new JsonPrimitive(value);
        return this.data.put(name, json);
    }

    /**
     * 设置指定Name的子元素， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
     */
    public Json set(String name, long value)
    {
        if(name == null) return null;
        return this.data.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的子元素， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
     */
    public Json set(String name, double value)
    {
        if(name == null) return null;
        return this.data.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的子元素， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
     */
    public Json set(String name, boolean value)
    {
        if(name == null) return null;
        return this.data.put(name, Json.getBooleanJson(value));
    }

    /**
     * 设置指定Name的子元素为NULL类型的实例， 如果不存在相应的Name，则相当于add()。
     * @param name 如果name为null，则不执行任何操作
     * @return 如果name不为null，则返回与name对应的先前子元素，否则null
     */
    public Json set(String name)
    {
        if(name == null) return null;
        return this.data.put(name, Json.nullJson);
    }

    /**
     * 批量设置子元素，但忽略name为null的entry。
     * @param map 批量子元素
     */
    public void setAll(Map<String, ? extends Json> map)
    {
        if(map.containsKey(null)) map.remove(null);
        this.data.putAll(map);
    }

    /**
     * 批量设置子元素，但忽略name为null的entry。
     * @param map 批量子元素
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
     * 删除指定Name的子元素。
     * @param name 子元素的Name
     */
    public void remove(String name)
    {
        this.data.remove(name);
    }
    
    /**
     * 判断是否存在指定Name的子元素。
     * @param name 子元素的名字
     * @return 存在返回true，否则返回false
     */
    public boolean containsName(String name)
    { 
        return this.data.containsKey(name);
    }
    
    /**
     * 返回Json实例所有子元素的Name组成的集合。
     * @return 所有子元素的Name
     */
    public Set<String> nameSet()
    {
        return this.data.keySet();
    }
    
    /**
     * 清除所有的子元素及其Name。
     */
    @Override
    public void clear()
    {
        this.data.clear();
    }
    
    /**
     * 判断Json实例是否不含任何子元素。
     * @return 不包含任何子元素返回true，否则返回false
     */
    @Override
    public boolean isEmpty()
    {
        return this.data.isEmpty();
    }
    
    /**
     * 返回所有的子元素的数量（按子元素的Name计算）。
     * @return 子元素的数量
     */
    @Override
    public int count()
    {
        return this.data.size();
    }
        
    /**
     * 生成Json文本，但不检测是否存在循环引用。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @return 对应的Json文本
     */
    @Override
    protected String generateJsonTextWithoutCheck(boolean useStandard)
    {        
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
                build.append(value.generateJsonTextWithoutCheck(useStandard));
            }
            
            cur++;
            if(cur != cnt) build.append(',');
        }
        build.append('}');
        
        return build.toString();
    }

    /**
     * 返回 Json实例类型 JsonType.OBJECT。
     */
    @Override
    public JsonType getType()
    {
        return JsonType.OBJECT;
    }
    
    /**
     * 返回Json实例对应的字符串。
     * @return 返回对应的标准Json文本
     */
    @Override
    public String getString()
    {
        return this.generateJsonText();
    }
    
    /**
     * 判断Json对象内是否存在循环引用
     * @param parentRef 上级Json对象的引用
     * @return 存在循环引用返回true，不存在返回false
     */
    @Override
    public boolean existsCircle(IdentityStack parentRef)
    {
        if(parentRef.contains(this)) return true;
        
        parentRef.push(this);
        boolean exists = false;
        
        Collection<Json> values = data.values();
        for(Json element: values)
        {
            exists = exists || element.existsCircle(parentRef);
        }
        
        parentRef.pop();
        return exists;
    }
}
