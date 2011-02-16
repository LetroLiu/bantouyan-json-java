package com.bantouyan.json;

/**
 * 用来生成Json实例（JsonObject或JsonArray）的接口，
 * 所有实现了该接口的对象都可以调用generateJson()方法获得对应的Json实例。
 * @author bantouyan
 * @version 1.00
 */
public interface Jsonable
{
    /**
     * 生成JsonObject实例或JsonArray实例，
     * 应该确保所生成的Json实例不会引用已存在的Json实例。
     * @return 所对应的JsonObject或JsonArray实例，返回null意味着无法从Jsonable实例
     * 生成Json实例
     */
    Json generateJson();
}
