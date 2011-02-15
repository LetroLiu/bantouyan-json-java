package com.bantouyan.json;

/**
 * 用来生成Json实例（JsonObject或JsonArray）的接口
 * @author bantouyan
 * @version 0.1
 */
public interface Jsonable
{
    /**
     * 生成JsonObject实例或JsonArray实例，
     * 应该确保所生成的Json实例及其子实例都不是对已存在的Json实例的引用。
     * @return
     */
    Json generateJson();
}
