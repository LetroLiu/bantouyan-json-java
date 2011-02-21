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
}
