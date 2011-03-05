package com.bantouyan.json;

/**
 * <p>用来生成Json实例（JsonObject或JsonArray）的接口，
 * 所有实现了该接口的对象都可以调用generateJson()方法获得对应的Json实例。
 * </p>
 * 
 * <p>与接口JsonParser不一样，Jsonable实例仅仅用generateJson方法把自身转换为Json实例，
 * 而不负责其他类型的Java对象，一般情况下用返回null表示解析失败。
 * </p>
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public interface Jsonable
{
    /**
     * 生成JsonObject实例或JsonArray实例，
     * 应该确保所生成的Json实例不会引用已存在的Json实例。
     * @return 所对应的JsonObject或JsonArray实例，
     * 返回null意味着无法从Jsonable实例生成Json实例
     */
    Json generateJson();
}
