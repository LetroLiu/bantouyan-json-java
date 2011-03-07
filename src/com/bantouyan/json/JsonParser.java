package com.bantouyan.json;

/**
 * <p>Json解析器，服务于类Json的静态方法parseJavaMap与parseJavaCollection。
 * 用于将Map的Key转换为JsonObject子元素的Name，
 * 将Map的Value与Collection的子元素转换为Json实例。
 * </p>
 * 
 * <p>与接口Jsonable不同的是，JsonParse解析器一般不负责将自身转换为Json实例，
 * 而是负责将其他类型的Java对象转换为Json实例，而且在解析失败时一般抛出
 * JsonException异常而不是仅仅返回空指针null。
 * </p>
 * 
 * <p>在执行parseJavaMap(map, parser)与parserJavaCollection(collection, parser)
 * 时，如果被解析的子元素是Jsonable实例，同时parser（JsonParser实例）
 * 也可以解析这个子元素，那么优先使用JsonParser解析。
 * </P>
 * 
 * @author 飞翔的河马
 * @version 1.01
 * @since 1.01
 */
public interface JsonParser
{
    /**
     * 判断Java对象是否可以转换为Json实例， 如果可以，则方法changeToJson返回对应的Json实例。
     * @param obj 被测试的Java对象
     * @return 可以返回true，不可以返回false
     */
    boolean canToJson(Object obj);
    
    /**
     * 将Java对象解析为Json实例。
     * 对于canToJson返回true的Java对象必须予以解析，返回false可不做处理。
     * @param obj 被解析的Java对象
     * @return 解析后的Json实例
     * @throws JsonException 解析失败时抛出
     */
    Json changeToJson(Object obj) throws JsonException;
    
    /**
     * 判断Java对象是否可以转换为JsonObect子元素的Name， 如果可以，
     * 则方法changeToName返回对象的String。
     * @param obj 被测试的Java对象
     * @return 可以返回true，不可以返回false
     */
    boolean canToName(Object obj);
    
    /**
     * 将Java对象解析为JsonObject子元素的Name。
     * @param obj 被解析的Java对象
     * @return 对应的可用作Name的String
     * @throws JsonException 解析失败
     */
    String changeToName(Object obj) throws JsonException;
}
