package com.bantouyan.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * <p>用来表示Json数组实例。Json数组是一组有序Json实例的集合，数组的每个子元素
 * 都有特定的<strong>下标（从0开始）</strong>，可以根据下标存取特定的子元素。</p>
 * 
 * <p><strong>创建JsonArray实例</strong>，除了可以通过调用Json类的parse开头
 * 的方法创建JsonArray实例外，还可以直接创建空的JsonArray实例，或从Json实例集合创建。
 * </p>
 * 
 * <p>调用方法<strong>get</strong>可以获取指定位置的子元素，方法<strong>getXXX
 * </strong>返回指定位置子元素的某种原始类型值，方法<strong>canToXXX</strong>
 * 判定指定位置的子元素是否可以转换为这种原始类型，重设指定位置的子元素调用方法
 * <strong>set</strong>，在数组尾追加子元素调用方法<strong>append</strong>，
 * 在指定位置插入子元素调用方法<strong>insert</strong>，批量添加子元素调用方法
 * <strong>addAll</strong>，删除指定位置的子元素调用方法<strong>remove</strong>。
 * </p>
 * 
 * <p>方法<strong>isEmpty</strong>可以判断JsonArray实例是否包含子元素，
 * 方法<strong>count</strong>返回子元素的的个数，方法<strong>clear</strong>
 * 可以清除所有的子元素。方法<strong>getType</strong> 返回JsonArray实例的类型JsonType.ARRAY。
 * </p>
 * 
 * @author bantouyan
 * @version 1.00
 */
public class JsonArray extends Json implements Iterable<Json>
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
     * 根据Json集合创建JsonArray实例
     * @param collection 源Json集合
     */
    public JsonArray(Collection<? extends Json> collection)
    {
        this.elements = new ArrayList<Json>();
        this.elements.addAll(collection);
    }
        
    /**
     * 获取指定位置的子元素。
     * @param index 子元素的位置
     * @return 对应子元素的值， 如果是null则返回 type是NULL类型的实例
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
     * 获取指定位置子元素的字符串值。
     * @param index 子元素的位置
     * @return 子元素对应的字符串值，如果子元素是JsonPrimitive类型则无双引号，否则是对应的标准Json文本
     */
    public String getString(int index)
    {
        Json json = get(index);
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
     * 判断指定位置的子元素是否可以转换为逻辑型（布尔型）值
     * @param index 子元素的位置
     * @return 可以返回true，否则返回false
     */
    public boolean canToBoolean(int index)
    {
        Json json = get(index);
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
     * 返回指定位置子元素的逻辑型（布尔型）值。
     * @param index 子元素的位置
     * @return 子元素对应的逻辑型（布尔型）值
     * @throws JsonException 如果子元素无法转换为逻辑型（布尔型）值则抛出异常
     */
    public boolean getBoolean(int index) throws JsonException
    {
        if(canToBoolean(index))
        {
            return ((JsonPrimitive)get(index)).getBoolean();
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to boolean value.");
        }
    }

    /**
     * 判断指定位置的子元素是否可以转换为整型值
     * @param index 子元素的位置
     * @return 可以返回true，否则返回false
     */
    public boolean canToLong(int index)
    {
        Json json = get(index);
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
     * 返回指定位置子元素的整型值。
     * @param index 子元素的位置
     * @return 子元素对应的整型值
     * @throws JsonException 如果子元素无法转换为整型值，则抛出异常
     */
    public long getLong(int index) throws JsonException
    {
        if(canToLong(index))
        {
            return ((JsonPrimitive)get(index)).getLong();
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to long value.");
        }
    }

    /**
     * 判断指定位置的子元素是否可以转换为浮点型值
     * @param index 子元素的位置
     * @return 可以返回true，否则返回false
     */
    public boolean canToDouble(int index)
    {
        Json json = get(index);
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
     * 返回指定位置子元素的浮点型值。
     * @param index 子元素的位置
     * @return 子元素对应的浮点型值
     * @throws JsonException 如果子元素无法转换为浮点型值，则抛出异常
     */
    public double getDouble(int index) throws JsonException
    {
        if(canToDouble(index))
        {
            return ((JsonPrimitive)get(index)).getDouble();
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to double value.");
        }
    }
    
    /**
     * 判断指定位置的子元素是否可以转换为JsonArray值
     * @param index 子元素的位置
     * @return 可以返回true，否则返回false
     */
    public boolean canToJsonArray(int index)
    {
        Json json = get(index);
        return (json instanceof JsonArray)? true: false;
    }
    
    /**
     * 返回指定位置子元素的JsonArray值。
     * @param index 子元素的位置
     * @return 子元素对应的浮点型值
     * @throws JsonException 如果子元素无法转换为浮点型值，则抛出异常
     */
    public JsonArray getJsonArray(int index) throws JsonException
    {
        if(canToJsonArray(index))
        {
            return (JsonArray)get(index);
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to JsonArray value.");
        }
    }
    
    /**
     * 判断指定位置的子元素是否可以转换为JsonObject值
     * @param index 子元素的位置
     * @return 可以返回true，否则返回false
     */
    public boolean canToJsonObject(int index)
    {
        Json json = get(index);
        return (json instanceof JsonObject)? true: false;
    }
    
    /**
     * 返回指定位置子元素的JsonObject值。
     * @param index 子元素的位置
     * @return 子元素对应的浮点型值
     * @throws JsonException 如果子元素无法转换为浮点型值，则抛出异常
     */
    public JsonObject getJsonObject(int index) throws JsonException
    {
        if(canToJsonObject(index))
        {
            return (JsonObject)get(index);
        }
        else
        {
            throw new JsonException("Cannot transfer element at " + index + " to JsonObject value.");
        }
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element 要添加的新元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(Json element)
    {
        return this.elements.add(element);
    } 
    
    /**
     * 向Json数组末尾添加一个新元素
     * @param element 要添加的新元素, 如果是null则抛出空指针异常
     * @return JsonArray实例是否发生改变
     */
    public boolean append(Jsonable element)
    {
        return this.elements.add(element.generateJson());
    }

    /**
     * 向Json数组末尾添加一个新元素。
     * @param element 要添加的新元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(String element)
    {
        Json json = (element == null)? Json.nullJson: new JsonPrimitive(element);
        return this.elements.add(json);
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element 要添加的新元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(long element)
    {
        return this.elements.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element 要添加的新元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(double element)
    {
        return this.elements.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element 要添加的新元素
     * @return JsonArray实例是否发生改变
     */
    public boolean append(boolean element)
    {
        return this.elements.add(Json.getBooleanJson(element));
    }
    
    /**
     * 向Json数组末尾添加一个NULL类型的Json实例
     * @return JsonArray实例是否发生改变
     */
    public boolean append()
    {
        return this.elements.add(Json.nullJson);
    }
    
    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     * @param element 要添加的新元素
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
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     * @param element 要添加的新元素, 如果是null则抛出空指针异常
     */
    public void insert(int index, Jsonable element)
    {
        this.elements.add(index, element.generateJson());
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     * @param element 要添加的新元素
     */
    public void insert(int index, String element)
    {
        if(element == null)
            this.elements.add(index, Json.nullJson);
        else
            this.elements.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     * @param element 要添加的新元素
     */
    public void insert(int index, long element)
    {
        this.elements.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     * @param element 要添加的新元素
     */
    public void insert(int index, double element)
    {
        this.elements.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     * @param element 要添加的新元素
     */
    public void insert(int index, boolean element)
    {
        this.elements.add(index, Json.getBooleanJson(element));
    }
    
    /**
     * 在指定位置向Json数组添加一个NULL元素，原来的元素依次后移。
     * @param index 新元素插入的位置
     */
    public void insert(int index)
    {
        this.elements.add(index, Json.nullJson);
    }
    
    /**
     * 向Json数组末尾批量添加元素。
     * @param list 要添加的元素
     */
    public void addAll(Collection<? extends Json> list)
    {
        this.elements.addAll(list);
    }
    
    /**
     * 在指定位置向Json数组批量添加元素，原来的元素依次后移。
     * @param index 指定的位置
     * @param list 要添加的元素
     */
    public void addAll(int index, Collection<? extends Json> list)
    {
        this.elements.addAll(index, list);
    }
    
    /**
     * 向Json数组末尾批量添加元素。
     * @param list 要添加的元素
     */
    public void addAllJsonable(Collection<? extends Jsonable> list)
    {
        for(Jsonable jsonable: list)
        {
            this.elements.add(jsonable.generateJson());
        }
    }
    
    /**
     * 在指定位置向Json数组批量添加元素，原来的元素依次后移。
     * @param index 指定的位置
     * @param list 要添加的元素
     */
    public void addAllJsonable(int index, Collection<? extends Jsonable> list)
    {
        Collection<Json> jsonList = new ArrayList<Json>(list.size());
        for(Jsonable jsonable: list)
        {
            jsonList.add(jsonable.generateJson());
        }
        this.elements.addAll(index, jsonList);
    }
    
    /**
     * 设置Json数组指定位置的元素。
     * @param index 指定的位置
     * @param element 子元素的新值
     */
    public void set(int index, Json element)
    {
        if(element == null)
            this.elements.set(index, Json.nullJson);
        else
            this.elements.set(index, element);
    }
    
    /**
     * 设置Json数组指定位置的元素。
     * @param index 指定的位置
     * @param element 子元素的新值, 如果是null则抛出空指针异常
     */
    public void set(int index, Jsonable element)
    {
        this.elements.set(index, element.generateJson());
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index 指定的位置
     * @param element 子元素的新值
     */
    public void set(int index, String element)
    {
        if(element == null)
            this.elements.set(index, Json.nullJson);
        else
            this.elements.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index 指定的位置
     * @param element 子元素的新值
     */
    public void set(int index, long element)
    {
        this.elements.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index 指定的位置
     * @param element 子元素的新值
     */
    public void set(int index, double element)
    {
        this.elements.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index 指定的位置
     * @param element 子元素的新值
     */
    public void set(int index, boolean element)
    {
        this.elements.set(index, Json.getBooleanJson(element));
    }

    /**
     * 设置Json数组指定位置的元素为NULL类型的Json实例。
     * @param index 指定的位置
     */
    public void set(int index)
    {
        this.elements.set(index, Json.nullJson);
    }
    
    /**
     * 删除Json数组指定位置的元素，后续的元素依次前移。
     * @param index 指定的位置
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
        else if(obj.getType() != JsonType.ARRAY)
        {
            return false;
        }
        else
        {
            int cnt = this.count();
            if(cnt != obj.count()) return false;
            
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
     * JsonArray实例的hash值，根据子元素的下标及其hashCode计算，
     * 不同的排列顺序（对应的JsonArray也不等）生成hashCode不同。
     * @return 根据对应的标准Json文本生成hash值
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
     * 返回Json数组的子元素个数。
     * @return Json数组内元素的个数
     */
    @Override
    public int count()
    {
        return this.elements.size();
    }
    
    /**
     * 清除Json数组中的所有元素。
     */
    @Override
    public void clear()
    {
        this.elements.clear();
    }
    
    /**
     * 判断Json数组是否为空数组。
     * @return 空数组返回true，否则返回false
     */
    @Override
    public boolean isEmpty()
    {
        return this.elements.isEmpty();
    }
    
    /**
     * 生成Json文本，并追加到参数builder的尾部
     * @param builder 保存Json文本的StringBuilder
     * @param useQuote 为true时Object的Name部分加引号， false时尽量不加引号
     */
    @Override
    protected void generateJsonText(StringBuilder builder, boolean useQuote)
    {
        builder.append('[');
        int cnt = elements.size();
        for(int i=0; i<cnt; i++)
        {
            Json element = elements.get(i);
            element.generateJsonText(builder, useQuote);
            if(i < cnt-1) builder.append(',');
        }
        builder.append(']');
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
     * 返回特定下标子元素的JsonType
     * @param index 子元素的下标
     * @return 子元素的Type
     */
    public JsonType getType(int index)
    {
        return get(index).getType();
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
     * 返回迭代器，用于for each循环
     * @return JsonArray的迭代器
     */
    @Override
    public Iterator<Json> iterator()
    {
        return elements.iterator();
    }
}
