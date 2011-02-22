package com.bantouyan.json.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import com.bantouyan.json.*;

/**
 * 测试Json解析功能
 * @author Administrator
 *
 */
public class TestParser
{
    @Test(expected = JsonParseException.class)
    public void parseJavaMapInvalidName() throws JsonParseException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(new Date(), "Date");
        Json json = Json.parseJavaMap(map);
    }
    
    @Test(expected = JsonParseException.class)
    public void parseJavaMapCircle() throws JsonParseException
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("self", map);
        Json json = Json.parseJavaMap(map);
    }
    
    @Test(expected = JsonParseException.class)
    public void parseJavaCollectionCircle() throws JsonParseException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(list);
        Json json = Json.parseJavaCollection(list);
    }
    
    @Test(expected = JsonParseException.class)
    public void parseJavaCircleA() throws JsonParseException
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        map.put("key", "value");
        list.add("element");
        map.put("list", list);
        list.add(map);
        Json json = Json.parseJavaCollection(list);
    }
    
    @Test(expected = JsonParseException.class)
    public void parseJavaCircleB() throws JsonParseException
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        map.put("key", "value");
        list.add("element");
        map.put("list", list);
        list.add(map);
        Json json = Json.parseJavaMap(map);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalidBegin() throws IOException, JsonParseException
    {
        String JsonText = "aa{";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalidEnd() throws IOException, JsonParseException
    {
        String JsonText = "[]sss";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void repeatedObjectName() throws IOException, JsonParseException
    {
        String JsonText = "{name1:\"value1\", \"name1\": \"value2\"}";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void objectNotEnd() throws IOException, JsonParseException
    {
        String JsonText = "{name1:\"value1\", \"name2\": \"value2\"";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void ArrayNotEnd() throws IOException, JsonParseException
    {
        String JsonText = "[1, 2, 3";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalidObjectNameBegin() throws IOException, JsonParseException
    {
        String JsonText = "{?name1:\"value1\", \"name1\": \"value2\"}";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalidObjectNameA() throws IOException, JsonParseException
    {
        String JsonText = "{null:null}";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalidObjectNameB() throws IOException, JsonParseException
    {
        String JsonText = "{nss&ssn:null}";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalidObjectNameEndA() throws IOException, JsonParseException
    {
        String JsonText = "{nss&:null}";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalidObjectNameEndB() throws IOException, JsonParseException
    {
        String JsonText = "{\"nss\"&:null}";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void noObjectName() throws IOException, JsonParseException
    {
        String JsonText = "{:null}";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void noObjectValue() throws IOException, JsonParseException
    {
        String JsonText = "{name: }";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalidObjectValueBegin() throws IOException, JsonParseException
    {
        String JsonText = "{name: ss\"ss\"}";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalidObjectValueEnd() throws IOException, JsonParseException
    {
        String JsonText = "{name: 356+}";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalidArrayValueBegin() throws IOException, JsonParseException
    {
        String JsonText = "[\"name\", ss\"ss\"]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalidArrayValueEnd() throws IOException, JsonParseException
    {
        String JsonText = "[\"name\", trueS]";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalid_true() throws IOException, JsonParseException
    {
        String JsonText = "[tRue]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_false() throws IOException, JsonParseException
    {
        String JsonText = "[fALSE]";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalid_null() throws IOException, JsonParseException
    {
        String JsonText = "[nULL]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_value() throws IOException, JsonParseException
    {
        String JsonText = "[TRUE]";
        Json json = Json.parseJsonText(JsonText);
    }

    @Test(expected = JsonParseException.class)
    public void invalid_namepart_noquote() throws IOException, JsonParseException
    {
        String JsonText = "{aa#d:30}";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_name_jswords() throws IOException, JsonParseException
    {
        String JsonText = "{class:\"Class\"}";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_character() throws IOException, JsonParseException
    {
        String JsonText = "[\"aaa\u0001bbb\"]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_unicode() throws IOException, JsonParseException
    {
        String JsonText = "[\"\\u03END\"]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_escapse() throws IOException, JsonParseException
    {
        String JsonText = "[\"\\U\"]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void stringNoEndQuote() throws IOException, JsonParseException
    {
        String JsonText = "[\"good]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_longA() throws IOException, JsonParseException
    {
        String JsonText = "[+30]";
        Json json = Json.parseJsonText(JsonText);
    }
    @Test(expected = JsonParseException.class)
    public void invalid_longA2() throws IOException, JsonParseException
    {
        String JsonText = "[00]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_longB() throws IOException, JsonParseException
    {
        String JsonText = "[+30L]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_doubleA() throws IOException, JsonParseException
    {
        String JsonText = "[-3.]";
        Json json = Json.parseJsonText(JsonText);
        System.out.println(json);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_doubleB() throws IOException, JsonParseException
    {
        String JsonText = "[-3.34E]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_doubleC() throws IOException, JsonParseException
    {
        String JsonText = "[-3.3E+]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_doubleD() throws IOException, JsonParseException
    {
        String JsonText = "[-3.5f]";
        Json json = Json.parseJsonText(JsonText);
    }
    
    @Test(expected = JsonParseException.class)
    public void invalid_doubleE() throws IOException, JsonParseException
    {
        String JsonText = "[-3.3e-4f]";
        Json json = Json.parseJsonText(JsonText);
    }
}
