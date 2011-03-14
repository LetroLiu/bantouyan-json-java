package com.bantouyan.json.test;

import org.junit.Test;
import org.junit.Assert;

import com.bantouyan.json.JsonPrimitive;
import com.bantouyan.json.Json.JsonType;

public class TestJsonPrimitive
{
    @Test
    public void testNull_clear_empty_count()
    {
        JsonPrimitive json = new JsonPrimitive();
        Assert.assertEquals(JsonType.NULL, json.getType());
        Assert.assertEquals("null", json.generateJsonText());
        Assert.assertFalse(json.canToBoolean());
        Assert.assertFalse(json.canToDouble());
        Assert.assertFalse(json.canToLong());
        
        //clear
        json.clear();
        Assert.assertEquals(JsonType.NULL, json.getType());
       
        //isEmpty count
        Assert.assertTrue(json.isEmpty());
        Assert.assertEquals(0, json.count());
    }
    
    @Test
    public void testBoolean()
    {
        JsonPrimitive json = new JsonPrimitive(true);
        Assert.assertEquals(JsonType.BOOLEAN, json.getType());
        Assert.assertEquals("true", json.generateJsonText());
        Assert.assertEquals(true, json.getBoolean());
        Assert.assertTrue(json.canToBoolean());
        Assert.assertFalse(json.canToDouble());
        Assert.assertFalse(json.canToLong());
    }
    
    @Test
    public void testDouble()
    {
        JsonPrimitive json = new JsonPrimitive(33.33);
        Assert.assertEquals(JsonType.FLOAT, json.getType());
        Assert.assertEquals("33.33", json.generateJsonText());
        Assert.assertEquals(33.33, json.getDouble(), 0.1e-9);
        Assert.assertFalse(json.canToBoolean());
        Assert.assertTrue(json.canToDouble());
        Assert.assertFalse(json.canToLong());
    }
    
    @Test
    public void testLong()
    {
        JsonPrimitive json = new JsonPrimitive(33);
        Assert.assertEquals(JsonType.INTEGER, json.getType());
        Assert.assertEquals("33", json.generateJsonText());
        Assert.assertEquals(33, json.getLong());
        Assert.assertFalse(json.canToBoolean());
        Assert.assertTrue(json.canToDouble());
        Assert.assertEquals(33.0, json.getDouble(), 0.1e-9);
        Assert.assertTrue(json.canToLong());
    }
    
    @Test
    public void testString()
    {
        JsonPrimitive json = new JsonPrimitive("str");
        Assert.assertEquals(JsonType.STRING, json.getType());
        Assert.assertEquals("str", json.getString());
        Assert.assertFalse(json.canToBoolean());
        Assert.assertFalse(json.canToDouble());
        Assert.assertFalse(json.canToLong());
        
        json = new JsonPrimitive("30");
        Assert.assertFalse(json.canToBoolean());
        Assert.assertTrue(json.canToDouble());
        Assert.assertEquals(30.0, json.getDouble(), 0.1e-9);
        Assert.assertTrue(json.canToLong());
        Assert.assertEquals(30, json.getLong());
        
        json = new JsonPrimitive("3.0e2");
        Assert.assertFalse(json.canToBoolean());
        Assert.assertTrue(json.canToDouble());
        Assert.assertEquals(300.0, json.getDouble(), 0.1e-9);
        Assert.assertFalse(json.canToLong());
        
        json = new JsonPrimitive("FALSE");
        Assert.assertTrue(json.canToBoolean());
        Assert.assertEquals(false, json.getBoolean());
        Assert.assertFalse(json.canToDouble());
        Assert.assertFalse(json.canToLong());
    }

}
