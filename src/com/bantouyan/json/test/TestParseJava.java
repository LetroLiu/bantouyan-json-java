package com.bantouyan.json.test;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Assert;

import com.bantouyan.json.*;

/**
 * 测试解析Java集合过程中产生的各种异常。
 */
public class TestParseJava
{
    @Test(expected = JsonException.class)
    public void mapInvalidName() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(new Date(), "Date");
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void mapInvalidValue() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("Date", new Date());
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void collectionInvalidValue() throws JsonException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(new Date());
        try
        {
            Json json = Json.parseJavaCollection(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void mapCircleA() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("self", map);
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java map is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void mapCircleB() throws JsonException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        list.add("element");
        list.add(map);
        map.put("key", "value");
        map.put("self", map);
        try
        {
            Json json = Json.parseJavaCollection(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java map is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void mapCircleC() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        map.put("key", "value");
        list.add("element");
        map.put("map", map);
        list.add(map);
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java map is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void collectionCircleA() throws JsonException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(list);
        try
        {
            Json json = Json.parseJavaCollection(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java colloection is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void collectionCircleB() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        map.put("key", "value");
        map.put("list", list);
        list.add("element");
        list.add(list);
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java colloection is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void collectionCircleC() throws JsonException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");
        map.put("list", list);
        list.add("element");
        list.add(map);
        try
        {
            Json json = Json.parseJavaCollection(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java colloection is referenced again."));
            throw e;
        }
    }

    @Test
    public void mapNormalA() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("NULL", null);
        map.put("INTEGER", 356);
        map.put("FLOAT", 34.1e2);
        map.put("BOOLEAN", true);
        map.put("STRING", "string");
        
        Json json = Json.parseJavaMap(map);
        System.out.println("===================================");
        String jsonStr = json.generateJsonText();
        System.out.println(jsonStr);
        Assert.assertEquals(5, json.count());
    }

    @Test
    public void mapNormalB() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        map.put("NULL", null);
        map.put("INTEGER", 356);
        map.put("FLOAT", 34.1e2);
        map.put("BOOLEAN", true);
        map.put("STRING", "string");
        map.put("LIST", list);
        list.add(false);
        list.add(null);
        Json json = Json.parseJavaMap(map);
        System.out.println("===================================");
        String jsonStr = json.generateJsonText();
        System.out.println(jsonStr);
        Assert.assertEquals(6, json.count());
    }

    @Test
    public void collectionNormalA() throws JsonException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(null);
        list.add(false);
        list.add("String");
        list.add(356);
        list.add(4.2e-1);
        
        Json json = Json.parseJavaCollection(list);
        System.out.println("===================================");
        String jsonStr = json.generateJsonText();
        System.out.println(jsonStr);
        Assert.assertEquals("[null,false,\"String\",356,0.42]", jsonStr);
    }

    @Test
    public void collectionNormalB() throws JsonException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(null);
        list.add(false);
        list.add("\"String\"");
        list.add(356);
        list.add(4.2e-1);
        list.add(map);
        map.put("NULL", null);
        
        Json json = Json.parseJavaCollection(list);
        System.out.println("===================================");
        String jsonStr = json.generateJsonText();
        System.out.println(jsonStr);
        Assert.assertEquals("[null,false,\"\\\"String\\\"\",356,0.42,{\"NULL\":null}]", jsonStr);
    }
    
    @Test
    public void testJsonParser()
    {
        JsonParser parser = new JsonParser(){
            public Json parseObjectToJson(Object obj) throws JsonException
            {
                if(obj instanceof Date)
                {
                    Date d = (Date)obj;
                    DateFormat df = DateFormat.getDateInstance();
                    String str = df.format(d);
                    return new JsonPrimitive(str);
                }
                else
                {
                    throw new JsonException();
                }
            }
        };
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(new Date());
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("date", new Date());
        
        JsonArray jary = Json.parseJavaCollection(list, parser);
        System.out.println(jary);
        Assert.assertEquals("[\"2011-3-5\"]", jary.generateJsonText());
        JsonObject jobj = Json.parseJavaMap(map, parser);
        System.out.println(jobj);
        Assert.assertEquals("{\"date\":\"2011-3-5\"}", jobj.generateJsonText());
    }
}
