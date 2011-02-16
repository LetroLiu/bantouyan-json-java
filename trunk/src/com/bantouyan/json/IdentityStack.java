package com.bantouyan.json;

/**
 * 使用==运算符来进行比较的堆栈。
 * 因为Java集合对象使用hashcode来进行比较，
 * 当Java集合内出现循环引用时hashcode方法会抛出StackOverflowError错误，
 * 所以开发了此类，仅供此Json库内部使用。
 * @author bantouyan
 * @version 1.00
 *
 */
class IdentityStack
{
    private Object[] datas;
    private int pos = 0;
    
    /**
     * 创建一个IdentityStack对象，默认容量为10。
     */
    public IdentityStack()
    {
        datas = new Object[10];
    }
    
    /**
     * 创建一个指定容量的IdentityStack对象。
     * @param capicity
     */
    public IdentityStack(int capicity)
    {
        datas = new Object[capicity];
    }
    
    /**
     * 添加一个新元素入栈，如果栈空间不够则自动增加。
     * @param e 入栈元素
     */
    public void push(Object e)
    {
        if(pos == datas.length)
        {
            Object[] ary = new Object[datas.length + 10];
            System.arraycopy(datas, 0, ary, 0, datas.length);
            datas = ary;
        }
        
        datas[pos] = e;
        pos++;
    }
    
    /**
     * 删除栈顶元素，如果是空栈则不执行任何操作。
     */
    public void pop()
    {
        if(pos >0)
        {
            pos--;
            datas[pos] = null; //remove element reference
        }
    }
    
    /**
     * 判断堆栈是否含有指定的元素。
     * @param e 要检查的元素
     * @return 如果堆栈内包含指定元素，则返回true，否则返回false。
     */
    public boolean contains(Object e)
    {
        for(int i=0; i<pos; i++)
        {
            if(datas[i] == e) return true;
        }
        return false;
    }
    
    public static void main(String[] args) throws Exception
    {
        /*
        String strAry = "[\"a\", \"b\", \"c\", true, null, false, 1, 2, 3]";
        String strObj = "{\"a\": \"A\", \"yes\": true, \"empty\": null, \"one\": 1}";
        
        //generate Json instance from String
        JsonArray jsonAry = (JsonArray)Json.parseJsonText(strAry);
        JsonObject jsonObj = (JsonObject)Json.parseJsonText(strObj);
        
        //generate Json Array instance
        JsonArray subAry = new JsonArray();
        subAry.append(); //add null
        subAry.append(true);
        subAry.insert(1, 345L);
        subAry.append(3.1415926);
        subAry.remove(0);
        
        //generate Json Object instance
        JsonObject subObj = new JsonObject();
        subObj.add("empty"); // add a null
        subObj.set("empty", "NULL");
        subObj.add("integer", 66);
        if(subObj.containsName("integer")) subObj.remove("integer");
        
        jsonAry.insert(3, jsonObj);
        jsonObj.add("list", subAry);
        jsonObj.set("obj", subObj);
        subAry.append(subObj);
        
        //next line will deal exception(circle reference)
        //subAry.append(jsonObj);
        
        //check circle reference
        if(jsonAry.existsCircle())
        {
            System.out.println("Exists circle reference in Json instance.");
        }
        
        String jsonText = jsonAry.generateJsonText(false);
        System.out.println(jsonText);  
*/    
    }
}
