package com.bantouyan.json;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>用来表示Json对象实例。Json对象是由子元素（即Name Value对）的无序集合构成，
 * 可以通过Name存取对应子元素的Value。</p>
 * 
 * <p>JsonObject不存在也不允许存在Name为null的子元素。<br/>
 * JsonObject不存在也不允许存在Value为null的子元素，
 * 如果添加的新子元素的Value为null，或者将子元素的Value改为null，
 * 则会自动转换为NULL类型的Json实例。</p>
 * 
 * <p><strong>创建JsonObject实例</strong>，
 * 除了可以通过调用Json类的parse开头的方法创建JsonObject实例外，
 * 还可以直接创建空的JsonObject实例，或从Json实例集合创建包含子元素的JsonObject实例。
 * </p>
 * 
 * <p>调用方法<strong>get</strong>可以获取指定Name的子元素，方法<strong>getXXX
 * </strong>以特定的类型返回指定Name的子元素的Value值，方法<strong>canToXXX</strong>
 * 判定指定Name的子元素是否可以转换为这种类型，重设指定Name的子元素的Value调用方法
 * <strong>set</strong>，批量重设调用方法<strong>setAll</strong>，
 * 添加子元素调用方法<strong>add</strong>，批量添加调用方法<strong>addAll</strong>，
 * 删除指定Name的子元素调用方法<strong>remove</strong>。
 * （方法<strong>add与set的区别</strong>是如果存在指定Name的子元素，方法add抛出异常，
 * 方法set覆盖子元素原来的Value；如果Name为null，方法add抛出异常，但set忽略这次操作）
 * </p>
 * 
 * <p>获取所有子元素的Name组成的集合调用方法<strong>nameSet</strong>，
 * 获取所有子元素的Value组成的集合调用方法<strong>values</strong>，
 * 获取所有子元素组成的集合调用方法<strong>entrySet</strong>。
 * </p>
 * 
 * <p>方法<strong>isEmpty</strong>可以判断JsonObject实例子元素的个数是否为零，
 * 方法<strong>count</strong>返回子元素的的个数，
 * 方法<strong>clear</strong>可以清除所有的子元素。
 * 方法<strong>getType</strong>返回JsonObject实例的类型JsonType.OBJECT。
 * </p>
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public final class JsonObject extends Json
{  
    //不允许出现key为null的entry，但允许value为null
    //值为null的value，get以及转换为Json文本时当类型为NULL的Json实例处理
    private HashMap<String, Json> elements = null;
    
    /**
     * 创建空的JsonObject实例。
     */
    public JsonObject()
    {
        this.elements = new HashMap<String, Json>();
    }
    
    /**
     * 创建具有初始容量（可容纳的子元素个数， 会自动变化）空的JsonObject实例。
     * @param initialCapicity 初始容量
     */
    public JsonObject(int initialCapicity)
    {
        //HashMap 默认的负载因子是0.75，故将初始容量的大小
        //设置为其四分之三等于initialCapicity
        int capicity = initialCapicity * 4 / 3 + 1;
        this.elements = new HashMap<String, Json>(capicity);
    }
    
    /**
     * 根据已有的Map创建包含子元素的JsonObject实例。
     * @param map 创建JsonObject的源数据，但忽略掉map中key为null的entry对
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public JsonObject(Map<?, ?> map) throws JsonException
    {
        this(map, null);
    }
    
    /**
     * 根据已有的Map创建包含子元素的JsonObject实例。
     * @param map 创建JsonObject的源数据，但忽略掉map中key为null的entry对
     * @param parser Json解析器，用于解析普通Java对象, 对于非空的key与value优先使用
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public JsonObject(Map<?, ?> map, JsonParser parser) throws JsonException
    {
        map.remove(null);
        IdentityStack parentRef = new IdentityStack();
        if(Json.haveCircle(map, parentRef))
        {
            throw new JsonException("Circle reference exists in this Map.");
        }
        
        this.elements = new HashMap<String, Json>(map.size() * 4 / 3 + 1);
        Set<?> keys = map.keySet();
        for(Object key: keys)
        {
            String nameStr = null;
            if(parser != null && parser.canToName(key))
            {
                nameStr = parser.changeToName(key);
            }
            else if(key instanceof String || key instanceof Number || key instanceof Boolean)
            {
                nameStr = key.toString();
            }
            else
            {
                throw new JsonException("Map key cannot cast to string.");
            }
            
            Object value = map.get(key);
            Json jsonValue = Json.changeToJson(value, parser);
            this.elements.put(nameStr, jsonValue);
        }
    }
    
    /**
     * 返回指定Name的子元素。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null，否则返回对应的子元素
     */
    public Json get(String name)
    {
        return ( (name == null) || (!this.elements.containsKey(name)))? null:
                    this.elements.get(name);
    }
    
    /**
     * 返回指定Name的子元素字符串值。
     * @param name 子元素的Name
     * @return 如果name为null，或不包含此name，则返回null
     */
    public String getString(String name)
    {
        if((name == null) || (!this.elements.containsKey(name))) return null;
        
        Json json = this.elements.get(name);
        return (json instanceof JsonPrimitive)? ((JsonPrimitive)json).getString():
                                                json.toString();
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为逻辑型（布尔型）值。
     * @param name 子元素的Name，如果name为null，或不包含此name，则返回false
     * @return 可以返回true，否则返回false
     */
    public boolean canToBoolean(String name)
    {
        if((name == null) || (!this.elements.containsKey(name))) return false;
        Json json = this.elements.get(name);
        
        return (json instanceof JsonPrimitive)? ((JsonPrimitive)json).canToBoolean():
                                               false;
    }
    
    /**
     * 返回指定Name的子元素的逻辑型（布尔型）值。
     * @param name 子元素的Name
     * @return 对应的逻辑型值
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public boolean getBoolean(String name) throws JsonException
    {
        if(canToBoolean(name))
        {
            return ((JsonPrimitive)this.elements.get(name)).getBoolean();
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to boolean value.");
        }
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为整型值。
     * @param name 子元素的Name，如果name为null，或不包含此name，则返回false
     * @return 可以返回true，否则返回false
     */
    public boolean canToLong(String name)
    {
        if((name == null) || (!this.elements.containsKey(name))) return false;
        Json json = this.elements.get(name);
        
        return (json instanceof JsonPrimitive)? ((JsonPrimitive)json).canToLong():
                                               false;
    }
    
    /**
     * 返回指定Name的子元素的整型值。
     * @param name 子元素的Name
     * @return 对应的整型值
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public long getLong(String name) throws JsonException
    {
        if(canToLong(name))
        {
            return ((JsonPrimitive)this.elements.get(name)).getLong();
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to long value.");
        }
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为浮点型值。
     * @param name 子元素的Name，如果name为null，或不包含此name，则返回false
     * @return 可以返回true，否则返回false
     */
    public boolean canToDouble(String name)
    {
        if((name == null) || (!this.elements.containsKey(name))) return false;
        Json json = this.elements.get(name);
        
        return (json instanceof JsonPrimitive)? ((JsonPrimitive)json).canToDouble():
                                               false;
    }
    
    /**
     * 返回指定Name的子元素的浮点型值。
     * @param name 子元素的Name
     * @return 对应的浮点型值
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public double getDouble(String name) throws JsonException
    {
        if(canToDouble(name))
        {
            return ((JsonPrimitive)this.elements.get(name)).getDouble();
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to double value.");
        }
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为JsonArray。
     * @param name 子元素的Name，如果name为null，或不包含此name，则返回false
     * @return 可以返回true，否则返回false
     */
    public boolean canToJsonArray(String name)
    {
        return ((name == null) || (!this.elements.containsKey(name)))? false:
            this.elements.get(name) instanceof JsonArray;
    }
    
    /**
     * 返回指定Name的子元素的JsonArray值。
     * @param name 子元素的Name
     * @return 对应的JsonArray实例
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public JsonArray getJsonArray(String name) throws JsonException
    {
        if(canToJsonArray(name))
        {
            return (JsonArray)this.elements.get(name);
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to JsonArray value.");
        }
    }
    
    /**
     * 判断指定Name的子元素是否可以转换为JsonObject。
     * @param name 子元素的Name，如果name为null，或不包含此name，则返回false
     * @return 可以返回true，否则返回false
     */
    public boolean canToJsonObject(String name)
    {
        return ((name == null) || (!this.elements.containsKey(name)))? false:
            this.elements.get(name) instanceof JsonObject;
    }
    
    /**
     * 返回指定Name的子元素的JsonObject值。
     * @param name 子元素的Name
     * @return 对应的JsonObject实例
     * @throws JsonException 如果name为null，或不存在此name，或类型不匹配，则抛出异常
     */
    public JsonObject getJsonObject(String name) throws JsonException
    {
        if(canToJsonObject(name))
        {
            return (JsonObject)this.elements.get(name);
        }
        else
        {
            throw new JsonException("Cannot transfer element corresponding " + name + " to JsonObject value.");
        }
    }
    
    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值，null被作为类型为NULL的Json实例处理
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, Json value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        if(value == null)
            this.elements.put(name, Json.nullJson);
        else
            this.elements.put(name, value);
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值，null被作为类型为NULL的Json实例处理
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, Jsonable value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        if(value == null)
        {
            this.elements.put(name, Json.nullJson);
        }
        else
        {
            Json json = value.generateJson();
            if(json == null)
                this.elements.put(name, Json.nullJson);
            else
                this.elements.put(name, json);
        }
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值，null被作为类型为NULL的Json实例处理
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, String value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        if(value == null)
            this.elements.put(name, Json.nullJson);
        else
            this.elements.put(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, long value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        this.elements.put(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, double value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        this.elements.put(name, new JsonPrimitive(value));
    }

    /**
     * 添加一个指定Name的新子元素。
     * @param name 子元素的Name
     * @param value 子元素的值
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name, boolean value) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        this.elements.put(name, Json.getBooleanJson(value));
    }

    /**
     * 添加一个指定Name的新子元素，子元素的Value是类型为NULL的Json实例。
     * @param name 子元素的Name
     * @throws JsonException 如果name为null，或已存在同名Name，则抛出异常
     */
    public void add(String name) throws JsonException
    {
        if(name == null)
        {
            throw new JsonException("Element Name in JsonObject cannot be null.");
        }
        else if(this.elements.containsKey(name))
        {
            String msg = "Name \"" + name + "\" already exist in this JsonObject.";
            throw new JsonException(msg);
        }
        
        this.elements.put(name, Json.nullJson);
    }
    
    /**
     * 批量添加子元素，值为null的value当做类型为NULL的Json实例处理。
     * @param map 包含批量子元素的Map
     * @throws JsonException 如果Map内含有循环引用或无法解析的对象，
     *  或试图加入同名子元素， 或加入Name为null的子元素，则抛出异常
     */
    public void addAll(Map<?, ?> map) throws JsonException
    {
        if(map.containsKey(null))
            throw new JsonException("Try to add element with name is null.");
        JsonObject jobj = Json.parseJavaMap(map);
        this.addAll(jobj);
    }
    
    /**
     * 批量添加子元素，值为null的value当做类型为NULL的Json实例处理。
     * @param map 包含批量子元素的Map
     * @param parser Json解析器，用于解析普通Java对象, 对于非空的key与value优先使用
     * @throws JsonException 如果Map内含有循环引用或无法解析的对象，
     *  或试图加入同名子元素， 或加入Name为null的子元素，则抛出异常
     */
    public void addAll(Map<? ,?> map, JsonParser parser) throws JsonException
    {
        if(map.containsKey(null))
            throw new JsonException("Try to add element with name is null.");
        JsonObject jobj = Json.parseJavaMap(map, parser);
        this.addAll(jobj);
    }
    
    /**
     * 批量添加子元素，子元素来自另外一个JsonObject实例
     * @param jobj 批量子元素的来源
     * @throws JsonException 如果有试图加入同名子元素，则抛出异常
     */
    public void addAll(JsonObject jobj) throws JsonException
    {
        String conflictNames = "";
        Set<String> names = jobj.nameSet();
        
        for(String name: names)
        {
            if(this.elements.containsKey(name))
                conflictNames += ", " + name;
        }
        
        if(conflictNames.equals(""))
        {
            this.elements.putAll(jobj.elements);
        }
        else
        {
            String msg = "Try to add exists names \""
                 + conflictNames.substring(2) + "\" to this JsonObject.";
            throw new JsonException(msg);
        }     
    }
    
    /**
     * 设置指定Name的子元素的Value， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值，null被作为类型为NULL的Json实例处理
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name, Json value)
    {
        if(name == null) return null;
        return (value == null)? this.elements.put(name, Json.nullJson):
                                this.elements.put(name, value);
    }

    /**
     * 设置指定Name的子元素的Value， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值，null被作为类型为NULL的Json实例处理
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name, Jsonable value)
    {
        if(name == null) return null;
        Json json = null;
        if(value != null) json = value.generateJson();
        return (json == null)? this.elements.put(name, Json.nullJson):
                                this.elements.put(name, json);
    }

    /**
     * 设置指定Name的子元素的Value， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值，null被作为类型为NULL的Json实例处理
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name, String value)
    {
        if(name == null) return null;
        return (value == null)? this.elements.put(name, Json.nullJson):
                                this.elements.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的子元素的Value， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name, long value)
    {
        return (name == null)? null:
                               this.elements.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的子元素的Value， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name, double value)
    {
        return (name == null)? null:
                               this.elements.put(name, new JsonPrimitive(value));
    }

    /**
     * 设置指定Name的子元素的Value， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @param value 子元素的新值
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name, boolean value)
    {
        return (name == null)? null:
                               this.elements.put(name, Json.getBooleanJson(value));
    }

    /**
     * 设置指定Name的子元素的Value为类型是NULL的Json实例， 如果存在同名Name则覆盖原来的Value。
     * @param name 如果name为null，则不执行任何操作
     * @return 如果name为null，则返回null，否则返回先前与name对应子元素的Value
     */
    public Json set(String name)
    {
        return (name == null)? null:
            this.elements.put(name, Json.nullJson);
    }

    /**
     * 批量设置子元素，值为null的value当做类型为NULL的Json实例处理，
     * 如果存在同名Name则覆盖原来的Value。
     * @param map 批量子元素，忽略name为null的entry
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常
     */
    public void setAll(Map<?, ?> map) throws JsonException
    {
        JsonObject nobj = Json.parseJavaMap(map);
        this.elements.putAll(nobj.elements);
    }
    
    /**
     * 批量设置子元素，值为null的value当做类型为NULL的Json实例处理，
     * 如果存在同名Name则覆盖原来的Value。
     * @param map 包含批量子元素的Map实例，忽略name为null的entry
     * @param parser Json解析器，用于解析普通Java对象, 对于非空的key与value优先使用
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常
     */
    public void setAll(Map<?, ?> map, JsonParser parser) throws JsonException
    {
        JsonObject nobj = Json.parseJavaMap(map, parser);
        this.elements.putAll(nobj.elements);
    }
    
    /**
     * 批量添加子元素， 子元素来自另外一个JsonObject实例，
     * 如果存在同名Name则覆盖原来的Value。
     * @param jobj 包含新子元素的JsonObject实例
     */
    public void setAll(JsonObject jobj)
    {
        this.elements.putAll(jobj.elements);
    }
    
    /**
     * 删除指定Name的子元素。
     * @param name 子元素的Name
     */
    public void remove(String name)
    {
        this.elements.remove(name);
    }
    
    /**
     * 判断是否存在指定Name的子元素。
     * @param name 子元素的Name
     * @return 存在返回true，否则返回false
     */
    public boolean containsName(String name)
    { 
        return this.elements.containsKey(name);
    }
    
    /**
     * 返回JsonObject实例所有子元素的Name组成的集合。
     * @return 所有子元素的Name组成的集合
     */
    public Set<String> nameSet()
    {
        return this.elements.keySet();
    }
    
    /**
     * 返回JsonObject实例所有子元素的Value组成的集合。
     * @return 所有子元素的Value组成的集合
     */
    //if modify this method, modify entrySet() together
    public Collection<Json> values()
    {
        return this.elements.values();
    }
    
    /**
     * 返回JsonObject实例所有子元素（Name Value对）组成的集合。
     * @return 所有子元素组成的集合
     */
    // if modify this method, modify values() together
    public Set<Entry<String, Json>> entrySet()
    {
        return elements.entrySet();
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
        else if(!(obj instanceof JsonObject))
        {
            return false;
        }
        else if(this.count() != obj.count())
        {
            return false;
        }
        else
        {
            JsonObject objObj = (JsonObject)obj;
            Set<String> nameSet = this.nameSet();
            
            for(String name: nameSet)
            {
                if(! objObj.containsName(name))
                {
                    return false;
                }
                else if(! this.get(name).same(objObj.get(name)))
                {
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * JsonObject实例的hash值，各子元素Name Value对的hashCode的和。
     * @return 根据对应的标准Json文本生成hash值
     */
    @Override
    public int hashCode()
    {
        int hashcode = 7;
        Set<String> names = nameSet();
        for(String name: names)
        {
            Json json = get(name);
            hashcode += name.hashCode();
            hashcode += json.hashCode() * 67;
            
        }
        return hashcode;
    }
    
    /**
     * 深层Clone一个JsonObject实例，Clone出来的新实例与原实例相等（euqals()返回true），
     * 但修改任何一个实例都不会影响另一个实例的值。
     * @return Clone出来的JsonObject实例
     */
    @Override
    public JsonObject clone()
    {
        JsonObject nval = (JsonObject)super.clone();
        
        @SuppressWarnings("unchecked")
        HashMap<String, Json> clone = (HashMap<String, Json>)this.elements.clone();
        nval.elements = clone;
        Set<String> names = this.elements.keySet();
        for(String name: names)
        {
            Json json = this.elements.get(name);
            if(! (json instanceof JsonPrimitive))
            {
                nval.elements.put(name, json.clone());
            }
        }
        
        return nval;
    }
    
    /**
     * 返回子元素的个数。
     * @return 子元素的个数
     */
    @Override
    public int count()
    {
        return this.elements.size();
    }
    
    /**
     * 清除所有的子元素。
     */
    @Override
    public void clear()
    {
        this.elements.clear();
    }
    
    /**
     * 判断Json实例子元素的个数是否为零。
     * @return 子元素的个数为零返回true，否则返回false
     */
    @Override
    public boolean isEmpty()
    {
        return this.elements.isEmpty();
    }

    /**
     * 向可追加对象追加Json文本。
     * @param dest 接受Json文本的可追加对象
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     * @throws IOException 追加字符流发生IO异常
     */
    @Override //if modify this method, modify appendToWriter() together
    protected void appendToAppendable(Appendable dest, boolean useQuote) throws IOException
    {
        int i = 0;
        
        dest.append('{');
        for(Entry<String, Json> entry: entrySet())
        {
            if(i > 0) dest.append(',');
            i++;
            
            String name = entry.getKey();
            if(useQuote)
            {
                JsonTextParser.jsonStringToAppendable(name, dest);
            }
            else
            {
                JsonTextParser.jsonStringToAppendableWithoutQutoe(name, dest);
            }
                
            dest.append(':');
            Json value = entry.getValue();
            //value is a json instance, not null, because it come from method entrySet()
            value.appendToAppendable(dest, useQuote);
        }
        dest.append('}');
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
     * 返回指定Name的子元素Value的JsonType。
     * @param name 元素的Name
     * @return 子元素Value的Type
     */
    public JsonType getType(String name)
    {
        if(name == null || (! this.elements.containsKey(name)))
            return null;
        else
            return this.elements.get(name).getType();
    }
    
    /**
     * 判断JsonObject实例内是否存在循环引用。
     * @param parentRef 上级Json对象的引用
     * @return 存在循环引用返回true，不存在返回false
     */
    @Override
    protected boolean existsCircle(IdentityStack parentRef)
    {
        if(parentRef.contains(this)) return true;
        
        parentRef.push(this);
        boolean exists = false;
        
        Collection<Json> values = elements.values();
        for(Json element: values)
        {
            exists = exists || element.existsCircle(parentRef);
        }
        
        parentRef.pop();
        return exists;
    }
}
