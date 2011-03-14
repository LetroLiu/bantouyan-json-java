package com.bantouyan.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>用来表示Json数组实例。Json数组是一组有序Json实例的集合，
 * 数组的每个子元素都有特定的<strong>下标（从0开始）</strong>，
 * 可以根据下标存取特定的子元素。</p>
 * 
 * <p><strong>创建JsonArray实例</strong>，
 * 除了可以通过调用Json类的parse开头的方法创建JsonArray实例外，
 * 还可以直接创建空的JsonArray实例，或从Json实例集合创建包含子元素的JsonO实例。
 * </p>
 * 
 * <p>调用方法<strong>get</strong>可以获取指定下标的子元素，方法<strong>getXXX
 * </strong>以特定的类型返回指定下标子元素的值，方法<strong>canToXXX</strong>
 * 判定指定下标的子元素是否可以转换为这种类型，重设指定下标的子元素调用方法
 * <strong>set</strong>，在数组尾追加子元素调用方法<strong>append</strong>，
 * 在指定下标插入子元素调用方法<strong>insert</strong>，批量添加子元素调用方法
 * <strong>addAll</strong>，删除指定下标的子元素调用方法<strong>remove</strong>。
 * </p>
 * 
 * <p>JsonArray实现了接口<strong>Iterable</strong>，可以用<strong>for each</strong>
 * 循环遍历所有的子元素，也可以用方法<strong>iterator</strong>获取JsonArray的迭代器。
 * </p>
 * 
 * <p>方法<strong>isEmpty</strong>可以判断JsonArray实例子元素的个数是否为零，
 * 方法<strong>count</strong>返回子元素的的个数，
 * 方法<strong>clear</strong>可以清除所有的子元素。
 * 方法<strong>getType</strong> 返回JsonArray实例的类型<strong>JsonType.ARRAY</strong>。
 * </p>
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public final class JsonArray extends Json implements Iterable<Json>
{
    //允许包含值为null的元素，但get及转换为Json文本时当NULL类型的Json实例处理
    private ArrayList<Json> elements;
    
    /**
     * 创建空的JsonArray实例。
     */
    public JsonArray()
    {
        this.elements = new ArrayList<Json>();
    }
    
    /**
     * 创建具有初始容量（可容纳的子元素个数， 会自动变化）空的JsonArray实例。
     * @param initialCapicity 初始容量
     */
    public JsonArray(int initialCapicity)
    {
        this.elements = new ArrayList<Json>(initialCapicity);
    }
    
    /**
     * 根据Json集合创建包含子元素的JsonArray实例。
     * @param collection 创建JsonArray的源数据
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public JsonArray(Collection<?> collection) throws JsonException
    {
        this(collection, null);
    }
    
    /**
     * 根据Json集合创建包含子元素的JsonArray实例。
     * @param collection 创建JsonArray的源数据
     * @param parser Json解析器，用于解析普通Java对象, 对于非空的key与value优先使用
     * @throws JsonException 如果Map内存在循环引用，或有无法解析的对象，则抛出异常。
     */
    public JsonArray(Collection<?> collection, JsonParser parser) throws JsonException
    {
        IdentityStack parentRef = new IdentityStack();
        if(Json.haveCircle(collection, parentRef))
        {
            throw new JsonException("Circle reference exists in this Collection.");
        }
        
        this.elements = new ArrayList<Json>(collection.size());
        for(Object value: collection)
        {
            Json element = Json.changeToJson(value, parser);
            this.elements.add(element);
        }
    }
        
    /**
     * 获取指定下标的子元素。
     * @param index 子元素的下标
     * @return 对应子元素的值
     */
    public Json get(int index)
    {
        Json json = this.elements.get(index);
        if(json == null)
        {
            json = Json.nullJson;
            this.elements.set(index, json);
        }
        
        return json;
    }
    
    /**
     * 获取指定下标子元素的字符串值。
     * @param index 子元素的下标
     * @return 子元素对应的字符串值
     */
    public String getString(int index)
    {
        Json json = this.elements.get(index);
        if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).getString();
        }
        else
        {
            return json.toString();
        }
    }
    
    /**
     * 判断指定下标的子元素是否可以转换为逻辑型（布尔型）值。
     * @param index 子元素的下标
     * @return 可以返回true，否则返回false
     */
    public boolean canToBoolean(int index)
    {
        Json json = this.elements.get(index);
        if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).canToBoolean();
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回指定下标子元素的逻辑型（布尔型）值。
     * @param index 子元素的下标
     * @return 子元素对应的逻辑型（布尔型）值
     * @throws JsonException 如果子元素无法转换为逻辑型（布尔型）值则抛出异常
     */
    public boolean getBoolean(int index) throws JsonException
    {
        if(canToBoolean(index))
        {
            return ((JsonPrimitive)this.elements.get(index)).getBoolean();
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to boolean value.");
        }
    }

    /**
     * 判断指定下标的子元素是否可以转换为整型值。
     * @param index 子元素的下标
     * @return 可以返回true，否则返回false
     */
    public boolean canToLong(int index)
    {
        Json json = this.elements.get(index);
        if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).canToLong();
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回指定下标子元素的整型值。
     * @param index 子元素的下标
     * @return 子元素对应的整型值
     * @throws JsonException 如果子元素无法转换为整型值，则抛出异常
     */
    public long getLong(int index) throws JsonException
    {
        if(canToLong(index))
        {
            return ((JsonPrimitive)this.elements.get(index)).getLong();
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to long value.");
        }
    }

    /**
     * 判断指定下标的子元素是否可以转换为浮点型值。
     * @param index 子元素的下标
     * @return 可以返回true，否则返回false
     */
    public boolean canToDouble(int index)
    {
        Json json = this.elements.get(index);
        if(json instanceof JsonPrimitive)
        {
            return ((JsonPrimitive)json).canToDouble();
        }
        else
        {
            return false;
        }
    }
    
    /**
     * 返回指定下标子元素的浮点型值。
     * @param index 子元素的下标
     * @return 子元素对应的浮点型值
     * @throws JsonException 如果子元素无法转换为浮点型值，则抛出异常
     */
    public double getDouble(int index) throws JsonException
    {
        if(canToDouble(index))
        {
            return ((JsonPrimitive)this.elements.get(index)).getDouble();
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to double value.");
        }
    }
    
    /**
     * 判断指定下标的子元素是否可以转换为JsonArray值。
     * @param index 子元素的下标
     * @return 可以返回true，否则返回false
     */
    public boolean canToJsonArray(int index)
    {
        Json json = this.elements.get(index);
        return (json instanceof JsonArray)? true: false;
    }
    
    /**
     * 返回指定下标子元素的JsonArray值。
     * @param index 子元素的下标
     * @return 子元素对应的JsonArray实例
     * @throws JsonException 如果子元素无法转换为浮点型值，则抛出异常
     */
    public JsonArray getJsonArray(int index) throws JsonException
    {
        if(canToJsonArray(index))
        {
            return (JsonArray)this.elements.get(index);
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to JsonArray value.");
        }
    }
    
    /**
     * 判断指定下标的子元素是否可以转换为JsonObject值。
     * @param index 子元素的下标
     * @return 可以返回true，否则返回false
     */
    public boolean canToJsonObject(int index)
    {
        Json json = this.elements.get(index);
        return (json instanceof JsonObject)? true: false;
    }
    
    /**
     * 返回指定下标子元素的JsonObject值。
     * @param index 子元素的下标
     * @return 子元素对应的JsonObject实例
     * @throws JsonException 如果子元素无法转换为浮点型值，则抛出异常
     */
    public JsonObject getJsonObject(int index) throws JsonException
    {
        if(canToJsonObject(index))
        {
            return (JsonObject)this.elements.get(index);
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to JsonObject value.");
        }
    }
    
    /**
     * 向Json数组末尾添加一个新子元素。
     * @param element 要添加的新子元素, null被作为类型为NULL的Json实例处理
     * @return JsonArray实例是否发生改变
     */
    public boolean append(Json element)
    {
        return (element == null)? this.elements.add(Json.nullJson):
                                  this. elements.add(element);
    } 
    
    /**
     * 向Json数组末尾添加一个新子元素，相当于append(element.generateJson())。
     * @param element 要添加的新子元素, null被作为类型为NULL的Json实例处理
     * @return JsonArray实例是否发生改变
     */
    public boolean append(Jsonable element)
    {
        Json json = (element == null)? Json.nullJson: element.generateJson();
        return (json == null)? this.elements.add(Json.nullJson):
                               this.elements.add(json);
    }

    /**
     * 向Json数组末尾添加一个新子元素。
     * @param element 要添加的新子元素, null被作为类型为NULL的Json实例处理
     * @return JsonArray实例是否发生改变
     */
    public boolean append(String element)
    {
        return (element == null)? this.elements.add(Json.nullJson):
                                  this.elements.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新子元素。
     * @param element 要添加的新子元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(long element)
    {
        return this.elements.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新子元素。
     * @param element 要添加的新子元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(double element)
    {
        return this.elements.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新子元素。
     * @param element 要添加的新子元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(boolean element)
    {
        return this.elements.add(Json.getBooleanJson(element));
    }
    
    /**
     * 向Json数组末尾添加一个类型为NULL的Json实例。
     * @return JsonArray实例是否发生改变
     */
    public boolean append()
    {
        return this.elements.add(Json.nullJson);
    }
    
    /**
     * 向Json数组末尾批量添加子元素
     * @param list 包含要添加子元素的的Collection实例
     * @throws JsonException 如果list内含有循环引用，或无法解析的Java对象，则抛出异常
     */
    public void appendAll(Collection<?> list) throws JsonException
    {
        JsonArray jary = Json.parseJavaCollection(list);
        this.elements.addAll(jary.elements);
    }
    
    /**
     * 向Json数组末尾批量添加子元素
     * @param list 包含要添加子元素的的Collection实例
     * @param parser Json解析器，用于解析普通Java对象, 对于非空的key与value优先使用
     * @throws JsonException 如果list内含有循环引用，或无法解析的Java对象，则抛出异常
     */
    public void appendAll(Collection<?> list, JsonParser parser) throws JsonException
    {
        JsonArray jary = Json.parseJavaCollection(list, parser);
        this.elements.addAll(jary.elements);
    }
    
    /**
     * 向Json数组末尾批量添加子元素
     * @param jary 包含要添加子元素的的JsonArray实例
     */
    public void appendAll(JsonArray jary)
    {
        this.elements.addAll(jary.elements);
    }
    
    /**
     * 在指定下标向Json数组添加一个新子元素，原来的子元素依次后移。
     * @param index 新元素的插入下标
     * @param element 要添加的新子元素, null被作为类型为NULL的Json实例处理
     */
    public void insert(int index, Json element)
    {
        if(element == null)
            this.elements.add(index, Json.nullJson);
        else
            this.elements.add(index, element);
    }
    
    /**
     * 
     * 在指定下标向Json数组添加一个新子元素，原来的子元素依次后移，
     * 相当于insert(index, element.generateJson())。
     * @param index 新子元素的插入下标
     * @param element 要添加的新子元素, null被作为类型为NULL的Json实例处理
     */
    public void insert(int index, Jsonable element)
    {
        Json json = (element == null)? Json.nullJson: element.generateJson();
        if(json == null)
            this.elements.add(index, Json.nullJson);
        else
            this.elements.add(index, json);
    }

    /**
     * 在指定下标向Json数组添加一个新子元素，原来的子元素依次后移。
     * @param index 新子元素的插入下标
     * @param element 要添加的新子元素, null被作为类型为NULL的Json实例处理
     */
    public void insert(int index, String element)
    {
        if(element == null)
            this.elements.add(index, Json.nullJson);
        else
            this.elements.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定下标向Json数组添加一个新子元素，原来的子元素依次后移。
     * @param index 新子元素的插入下标
     * @param element 要添加的新子元素
     */
    public void insert(int index, long element)
    {
        this.elements.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定下标向Json数组添加一个新子元素，原来的子元素依次后移。
     * @param index 新子元素的插入下标
     * @param element 要添加的子元素
     */
    public void insert(int index, double element)
    {
        this.elements.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定下标向Json数组添加一个新子元素，原来的子元素依次后移。
     * @param index 新子元素的插入下标
     * @param element 要添加的新子子元素
     */
    public void insert(int index, boolean element)
    {
        this.elements.add(index, Json.getBooleanJson(element));
    }
    
    /**
     * 在指定下标向Json数组添加一个类型为NULL的Json实例，原来的子元素依次后移。
     * @param index 新子元素的插入下标
     */
    public void insert(int index)
    {
        this.elements.add(index, Json.nullJson);
    }
    
    /**
     * 向Json数组末尾批量添加子元素
     * @param list 包含要添加子元素的的Collection实例
     * @throws JsonException 如果list内含有循环引用，或无法解析的Java对象，则抛出异常
     */
    public void insertAll(int index, Collection<?> list) throws JsonException
    {
        JsonArray jary = Json.parseJavaCollection(list);
        this.elements.addAll(index, jary.elements);
    }
    
    /**
     * 在指定下标向Json数组末尾批量添加子元素
     * @param list 包含要添加子元素的的Collection实例
     * @param parser Json解析器，用于解析普通Java对象, 对于非空的key与value优先使用
     * @throws JsonException 如果list内含有循环引用，或无法解析的Java对象，则抛出异常
     */
    public void insertAll(int index, Collection<?> list, JsonParser parser) throws JsonException
    {
        JsonArray jary = Json.parseJavaCollection(list, parser);
        this.elements.addAll(index, jary.elements);
    }
    
    /**
     * 在指定下标向Json数组末尾批量添加子元素
     * @param jary 包含要添加子元素的的JsonArray实例
     */
    public void insertAll(int index, JsonArray jary)
    {
        this.elements.addAll(index, jary.elements);
    }
    
    /**
     * 设置Json数组指定下标的子元素。
     * @param index 指定的下标
     * @param element 子元素的新值, null被作为类型为NULL的Json实例处理
     */
    public void set(int index, Json element)
    {
        if(element == null)
            this.elements.set(index, Json.nullJson);
        else
            this.elements.set(index, element);
    }
    
    /**
     * 设置Json数组指定下标的子元素。
     * @param index 指定的下标
     * @param element 子元素的新值, null被作为类型为NULL的Json实例处理
     */
    public void set(int index, Jsonable element)
    {
        Json json = (element == null)? Json.nullJson: element.generateJson();
        if(json == null)
            this.elements.set(index, Json.nullJson);
        else
            this.elements.set(index, json);
    }

    /**
     * 设置Json数组指定下标的子元素。
     * @param index 指定的下标
     * @param element 子元素的新值, null被作为类型为NULL的Json实例处理
     */
    public void set(int index, String element)
    {
        if(element == null)
            this.elements.set(index, Json.nullJson);
        else
            this.elements.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定下标的子元素。
     * @param index 指定的下标
     * @param element 子元素的新值
     */
    public void set(int index, long element)
    {
        this.elements.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定下标的子元素。
     * @param index 指定的下标
     * @param element 子元素的新值
     */
    public void set(int index, double element)
    {
        this.elements.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定下标的子元素。
     * @param index 指定的下标
     * @param element 子元素的新值
     */
    public void set(int index, boolean element)
    {
        this.elements.set(index, Json.getBooleanJson(element));
    }

    /**
     * 设置Json数组指定下标的子元素为类型是NULL的Json实例。
     * @param index 指定的下标
     */
    public void set(int index)
    {
        this.elements.set(index, Json.nullJson);
    }
    
    /**
     * 删除Json数组指定下标的子元素，后续的子元素依次前移。
     * @param index 指定的下标
     */
    public void remove(int index)
    {
        this.elements.remove(index);
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
        else if(!(obj instanceof JsonArray))
        {
            return false;
        }
        else if(this.count() != obj.count())
        {
            return false;
        }
        else
        {
            int cnt = this.count();
            
            JsonArray objAry = (JsonArray)obj;
            for(int i=0; i<cnt; i++)
            {
                if(! this.get(i).same(objAry.get(i)))
                {
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * JsonArray实例的hash值，根据子元素及其下标计算hashCode，
     * 不同的排列顺序（对应的JsonArray也不等）生成hashCode不同。
     * @return 对应的hashCode值
     */
    @Override
    public int hashCode()
    {
        int hashcode = 7;
        int cnt = count();
        for(int i=1; i<cnt; i++)
        {
            Json json = get(i);
            hashcode *= 67;
            hashcode += json.hashCode();
        }
        return hashcode;
    }
    
    /**
     * 深层Clone一个JsonArray实例，Clone出来的新实例与原实例相等（euqals()返回true），
     * 但修改任何一个实例都不会影响另一个实例的值。
     * @return Clone出来的JsonArray实例
     */
    @Override
    public JsonArray clone()
    {
        JsonArray nval = (JsonArray)super.clone();
        
        @SuppressWarnings("unchecked")
        ArrayList<Json> clone = (ArrayList<Json>)this.elements.clone();
        nval.elements = clone;
        for(int i=0; i<this.elements.size(); i++)
        {
            Json json= this.elements.get(i);
            if(! (json instanceof JsonPrimitive))
            {
                nval.elements.set(i, json.clone());
            }
        }
        
        return nval;
    }
    
    /**
     * 返回Json数组的子元素个数。
     * @return Json数组内子元素的个数
     */
    @Override
    public int count()
    {
        return this.elements.size();
    }
    
    /**
     * 清除Json数组中所有的子元素。
     */
    @Override
    public void clear()
    {
        this.elements.clear();
    }
    
    /**
     * 判断Json实例子元素的个数是否为零（即是否为空数组）。
     * @return 子元素的个数为零（空数组）返回true，否则返回false
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
    @Override
    protected void appendToAppendable(Appendable dest, boolean useQuote) throws IOException
    {
        dest.append('[');
        int cnt = count();
        for(int i=0; i<cnt; i++)
        {
            if(i > 0) dest.append(',');
            Json element = get(i);
            element.appendToAppendable(dest, useQuote);
        }
        dest.append(']');
    }
    
    /**
     * 返回 Json实例类型 JsonType.ARRAY。
     */
    @Override
    public JsonType getType()
    {
        return JsonType.ARRAY;
    }
    
    /**
     * 返回特定下标子元素的JsonType。
     * @param index 子元素的下标
     * @return 子元素的Type
     */
    public JsonType getType(int index)
    {
        return this.elements.get(index).getType();
    }

    /**
     * 判断JsonArray实例内是否存在循环引用。
     * @param parentRef 上级Json对象堆栈，用于检测循环引用
     * @return 有循环引用返回true，否则返回false
     */
    @Override
    protected boolean existsCircle(IdentityStack parentRef)
    {
        if(parentRef.contains(this)) return true;
        
        parentRef.push(this);
        boolean exists = false;
        
        for(Json element: elements)
        {
            exists = exists || element.existsCircle(parentRef);
        }
        
        parentRef.pop();
        return exists;
    }

    /**
     * 返回迭代器，用于for each循环。
     * @return JsonArray的迭代器
     */
    @Override
    public Iterator<Json> iterator()
    {
        return elements.iterator();
    }
}
