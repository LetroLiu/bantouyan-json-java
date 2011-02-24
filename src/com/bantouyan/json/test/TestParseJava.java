package com.bantouyan.json.test;

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
    @Test(expected = JsonParseException.class)
    public void mapInvalidName() throws JsonParseException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(new Date(), "Date");
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void mapInvalidValue() throws JsonParseException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("Date", new Date());
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void collectionInvalidValue() throws JsonParseException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(new Date());
        try
        {
            Json json = Json.parseJavaCollection(list);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void mapCircleA() throws JsonParseException
    {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("self", map);
        try
        {
            Json json = Json.parseJavaMap(map);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java map is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void mapCircleB() throws JsonParseException
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
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java map is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void mapCircleC() throws JsonParseException
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
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java map is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void collectionCircleA() throws JsonParseException
    {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(list);
        try
        {
            Json json = Json.parseJavaCollection(list);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java colloection is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void collectionCircleB() throws JsonParseException
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
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java colloection is referenced again."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void collectionCircleC() throws JsonParseException
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
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Java colloection is referenced again."));
            throw e;
        }
    }

    @Test
    public void mapNormalA() throws JsonParseException
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
    public void mapNormalB() throws JsonParseException
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
    public void collectionNormalA() throws JsonParseException
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
    public void collectionNormalB() throws JsonParseException
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
}
