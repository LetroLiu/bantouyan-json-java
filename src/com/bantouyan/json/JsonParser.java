package com.bantouyan.json;

/**
 * <p>Json解析器，用来将普通Java对象解析成Json实例。
 * 该接口的实例用于类Json的静态方法parseJavaMap与parseJavaCollection。
 * 用于为除String、Number、Boolean、Collection与Map之外其他不能直接转换为
 * Json 实例的Java对象提供一个解析器，将这些对象解析成Json实例。
 * </p>
 * 
 * <p>与接口Jsonable不同的是，JsonParse解析器一般不负责将自身转换为Json实例，
 * 而是负责将其他类型的Java对象转换为Json实例，而且在解析失败时一般抛出
 * JsonException异常而不是仅仅返回空指针null。
 * </p>
 * 
 * <p>在执行parseJavaMap(map, parser)与parserJavaCollection(collection, parser)
 * 时，如果被解析的子元素是Jsonable实例，同时parser（JsonParser实例）
 * 也可以解析这个子元素，那么使用Jsonable的方法generateJson。
 * </P>
 * 
 * @author 飞翔的河马
 * @version 1.00
 */
public interface JsonParser
{
    /**
     * 将Java对象解析为Json实例，当解析失败时应该抛出JsonException异常。
     * @param obj 被解析的Java对象（解析String、Number、Boolean、Collection与Map无效）
     * @return 解析后的Json实例
     * @throws JsonException 解析失败时抛出
     */
    Json parseObjectToJson(Object obj) throws JsonException;
}
