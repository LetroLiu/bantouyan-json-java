package com.bantouyan.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 用来表示Json数组实例。
 * @author bantouyan
 * @version 0.1
 */
public class JsonArray extends Json
{
    //允许包含值为null的元素，但get及转换为Json文本时当NULL类型的Json实例处理
    private ArrayList<Json> data;
    
    public JsonArray()
    {
        this.data = new ArrayList<Json>();
    }
    
    public JsonArray(ArrayList<Json> data)
    {
        this.data = data;
    }
    
    /**
     * 获取指定位置的Json实例， 如果是null则返回 type是NULL类型的实例。
     * @param index
     * @return
     */
    public Json get(int index)
    {
        Json json = this.data.get(index);
        if(json == null)
        {
            json = Json.nullJson;
            this.data.set(index, json);
        }
        
        return json;
    }
    
    /**
     * 返回指定位置Json实例的字符串值。
     * @param index
     * @return
     */
    public String getString(int index)
    {
        return get(index).getString();
    }
    
    /**
     * 返回指定位置Json实例的逻辑型（布尔型）值。
     * @param index
     * @return
     * @throws JsonException
     */
    public boolean getBoolean(int index) throws JsonException
    {
        return get(index).getBoolean();
    }

    /**
     * 返回指定位置Json实例的整型值。
     * @param index
     * @return
     * @throws JsonException
     */
    public long getLong(int index) throws JsonException
    {
        return get(index).getLong();
    }

    /**
     * 返回指定位置Json实例的浮点型值。
     * @param index
     * @return
     * @throws JsonException
     */
    public double getDouble(int index) throws JsonException
    {
        return get(index).getDouble();
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element
     * @return JsonArray是否发生变动
     */
    public boolean append(Json element)
    {
        return this.data.add(element);
    }    

    /**
     * 向Json数组末尾添加一个新元素。
     * @param element
     * @return JsonArray是否发生变动
     */
    public boolean append(Jsonable element)
    {
        if(element == null)
            return this.data.add(Json.nullJson);
        
        return this.data.add(element.generateJson());
    }

    /**
     * 向Json数组末尾添加一个新元素。
     * @param element
     * @return JsonArray是否发生变动
     */
    public boolean append(String element)
    {
        Json json = (element == null)? Json.nullJson: new JsonPrimitive(element);
        return this.data.add(json);
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element
     * @return JsonArray是否发生变动
     */
    public boolean append(long element)
    {
        return this.data.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element
     * @return JsonArray是否发生变动
     */
    public boolean append(double element)
    {
        return this.data.add(new JsonPrimitive(element));
    }
    
    /**
     * 向Json数组末尾添加一个新元素。
     * @param element
     * @return JsonArray是否发生变动
     */
    public boolean append(boolean element)
    {
        return this.data.add(Json.getBooleanJson(element));
    }
    
    /**
     * 向Json数组末尾添加一个NULL类型的Json实例
     * @return JsonArray是否发生变动
     */
    public boolean append()
    {
        return this.data.add(Json.nullJson);
    }
    
    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index
     * @param element
     */
    public void insert(int index, Json element)
    {
        if(element == null)
            this.data.add(Json.nullJson);
        else
            this.data.add(index, element);
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index
     * @param element
     */
    public void insert(int index, Jsonable element)
    {
        if(element == null)
            this.data.add(Json.nullJson);
        else
            this.data.add(index, element.generateJson());
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index
     * @param element
     */
    public void insert(int index, String element)
    {
        if(element == null)
            this.data.add(index, Json.nullJson);
        else
            this.data.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index
     * @param element
     */
    public void insert(int index, long element)
    {
        this.data.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index
     * @param element
     */
    public void insert(int index, double element)
    {
        this.data.add(index, new JsonPrimitive(element));
    }

    /**
     * 在指定位置向Json数组添加一个新元素，原来的元素依次后移。
     * @param index
     * @param element
     */
    public void insert(int index, boolean element)
    {
        this.data.add(index, Json.getBooleanJson(element));
    }
    
    /**
     * 在指定位置向Json数组添加一个NULL元素，原来的元素依次后移。
     * @param index
     */
    public void insert(int index)
    {
        this.data.add(index, Json.nullJson);
    }
    
    /**
     * 向Json数组末尾批量添加元素。
     * @param list
     */
    public void addAll(Collection<? extends Json> list)
    {
        this.data.addAll(list);
    }
    
    /**
     * 在指定位置向Json数组批量添加元素，原来的元素依次后移。
     * @param index
     * @param list
     */
    public void addAll(int index, Collection<? extends Json> list)
    {
        this.data.addAll(index, list);
    }
    
    /**
     * 向Json数组末尾批量添加元素。
     * @param list
     */
    public void addAllJsonable(Collection<? extends Jsonable> list)
    {
        for(Jsonable jsonable: list)
        {
            this.data.add(jsonable.generateJson());
        }
    }
    
    /**
     * 在指定位置向Json数组批量添加元素，原来的元素依次后移。
     * @param index
     * @param list
     */
    public void addAllJsonable(int index, Collection<? extends Jsonable> list)
    {
        Collection<Json> jsonList = new ArrayList<Json>(list.size());
        for(Jsonable jsonable: list)
        {
            jsonList.add(jsonable.generateJson());
        }
        this.data.addAll(index, jsonList);
    }
    
    /**
     * 设置Json数组指定位置的元素。
     * @param index
     * @param element
     */
    public void set(int index, Json element)
    {
        this.data.set(index, element);
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index
     * @param element
     */
    public void set(int index, Jsonable element)
    {
        if(element == null)
            this.data.set(index, Json.nullJson);
        else
            this.data.set(index, element.generateJson());
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index
     * @param element
     */
    public void set(int index, String element)
    {
        if(element == null)
            this.data.set(index, Json.nullJson);
        else
            this.data.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index
     * @param element
     */
    public void set(int index, long element)
    {
        this.data.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index
     * @param element
     */
    public void set(int index, double element)
    {
        this.data.set(index, new JsonPrimitive(element));
    }

    /**
     * 设置Json数组指定位置的元素。
     * @param index
     * @param element
     */
    public void set(int index, boolean element)
    {
        this.data.set(index, Json.getBooleanJson(element));
    }

    /**
     * 设置Json数组指定位置的元素为NULL类型的Json实例。
     * @param index
     */
    public void set(int index)
    {
        this.data.set(index, Json.nullJson);
    }
    
    /**
     * 删除Json数组指定位置的元素，后续的元素依次前移。
     * @param index
     */
    public void remove(int index)
    {
        this.data.remove(index);
    }
    
    /**
     * 返回Json数组的元素个数。
     * @return
     */
    @Override
    public int count()
    {
        return this.data.size();
    }
    
    /**
     * 清楚Json数组中的所有元素。
     */
    @Override
    public void clear()
    {
        this.data.clear();
    }
    
    /**
     * 判断Json数组是否为空数组。
     * @return
     */
    @Override
    public boolean isEmpty()
    {
        return this.data.isEmpty();
    }
    
    /**
     * 生成Json文本。
     * @param useStandard true生成标准文本，false则尝试在Object的name部分不加引号
     * @return
     */
    @Override
    protected String generateJsonTextWithoutCheck(boolean useStandard)
    {
        StringBuilder build = new StringBuilder();
        
        build.append('[');
        int cnt = data.size();
        for(int i=0; i<cnt; i++)
        {
            Json element = data.get(i);
            build.append(element.generateJsonTextWithoutCheck(useStandard));
            if(i < cnt-1) build.append(',');
        }
        build.append(']');
        
        return build.toString();
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
     * 返回Json 实例的字符串值。
     * @return
     */
    @Override
    public String getString()
    {
        return this.toString();
    }

    /**
     * 返回Json实例的整型值。
     * @return
     * @throws JsonException
     */
    @Override
    public long getLong() throws JsonException
    {
        throw new JsonException("Cannot transfer JsonObject to long value.");
    }

    /**
     * 返回Json实例的浮点型值。
     * @return
     * @throws JsonException
     */
    @Override
    public double getDouble() throws JsonException
    {
        throw new JsonException("Cannot transfer JsonObject to double value.");
    }

    /**
     * 返回Json实例的逻辑型（布尔型）值。
     * @return
     * @throws JsonException
     */
    @Override
    public boolean getBoolean() throws JsonException
    {
        throw new JsonException("Cannot transfer JsonObject to boolean value.");
    }

    /**
     * 判断Json对象内是否存在循环引用
     * @param parentRef 上级Json对象的引用
     * @return
     */
    @Override
    public boolean existsCircle(IdentityStack parentRef)
    {
        if(parentRef.contains(this)) return true;
        
        parentRef.push(this);
        boolean exists = false;
        
        for(Json element: data)
        {
            exists = exists || element.existsCircle(parentRef);
        }
        
        parentRef.pop();
        return exists;
    }
}
