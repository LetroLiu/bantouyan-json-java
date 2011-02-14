package com.bantouyan.json;

/**
 * 用来生成Json对象（JsonObject或JsonArray）的接口
 * @author bantouyan
 * @version 0.1
 */
public interface Jsonable
{
    /**
     * 生成JsonObject对象或JsonArray对象
     * @return
     */
    Json generateJson();
}
