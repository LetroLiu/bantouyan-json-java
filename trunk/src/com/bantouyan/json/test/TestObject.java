package com.bantouyan.json.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.*;

import com.bantouyan.json.*;
import com.bantouyan.json.Json.JsonType;

public class TestObject
{
    @Test
    public void instance()
    {
        JsonObject jobj = new JsonObject();
        Assert.assertTrue(jobj.isEmpty());
        Assert.assertEquals(0, jobj.count());
        
        HashMap<String, Json> map = new HashMap<String, Json>();
        map.put("object", new JsonObject());
        map.put("array", new JsonArray());
        jobj = new JsonObject(map);
        Assert.assertFalse(jobj.isEmpty());
        Assert.assertEquals(2, jobj.count());
        Set<String> nameSet = jobj.nameSet();
        HashSet<String> expSet = new HashSet<String>();
        expSet.add("object");
        expSet.add("array");
        Assert.assertEquals(expSet, nameSet);
        
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(jobj.getType("object"), jobj.get("object").getType());
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("array"));
        Assert.assertEquals(jobj.getType("array"), jobj.get("array").getType());
        Assert.assertTrue(jobj.containsName("object"));
        Assert.assertFalse(jobj.containsName("false"));
        Assert.assertFalse(jobj.containsName(null));
        
        jobj.remove("object");
        Assert.assertFalse(jobj.containsName("object"));
        Assert.assertEquals(1, jobj.count());
        
        jobj.clear();
        Assert.assertTrue(jobj.isEmpty());
        Assert.assertEquals(0, jobj.count());
        
        map = null;
        boolean error = false;
        try
        {
            jobj = new JsonObject(map);
        } 
        catch (NullPointerException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
    }

    @Test
    public void addXXX()
    {
        JsonObject jobj = new JsonObject();
        int cnt = 0;
        
        //add null
        jobj.add("null");
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("null"));
        Assert.assertEquals("null", jobj.getString("null"));
        
        //add boolean
        jobj.add("boolean", true);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.BOOLEAN, jobj.getType("boolean"));
        Assert.assertEquals(true, jobj.getBoolean("boolean"));
        
        //add long
        jobj.add("integer", 66);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.INTEGER, jobj.getType("integer"));
        Assert.assertEquals(66L, jobj.getLong("integer"));
        
        //add double
        jobj.add("float", 666.66);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.FLOAT, jobj.getType("float"));
        Assert.assertEquals(666.66, jobj.getDouble("float"), 0.1e-10);
        
        //add string
        String str = "String";
        jobj.add("string", str);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.STRING, jobj.getType("string"));
        Assert.assertEquals(str, jobj.getString("string"));
        
        str = null;
        jobj.add("string2", str);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("string2"));
        
        //add json
        JsonObject json = new JsonObject();
        jobj.add("object", json);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(json, jobj.get("object"));
        
        json = null;
        jobj.add("object2", json);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("object2"));
        
        //add jsonable
        Jsonable jn  = new Jsonable(){public Json generateJson(){return new JsonArray();}};
        jobj.add("jsonable", jn);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("jsonable"));
        Assert.assertEquals(jn.generateJson(), jobj.get("jsonable"));
        
        jn = null;
        jobj.add("jsonable2", jn);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("jsonable2"));
        
        //add exception -- name repeat
        boolean error = false;
        try
        {
            jobj.add("null", "NULL:");
        } 
        catch (JsonException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
        Assert.assertEquals(cnt, jobj.count());
        
    }
    
    @Test
    public void setXXX()
    {
        JsonObject jobj = new JsonObject();
        int cnt = 0;
        
        //set null
        jobj.set("null");
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("null"));
        Assert.assertEquals("null", jobj.getString("null"));
        
        //set boolean
        jobj.set("boolean", true);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.BOOLEAN, jobj.getType("boolean"));
        Assert.assertEquals(true, jobj.getBoolean("boolean"));
        
        //set long
        jobj.set("integer", 66);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.INTEGER, jobj.getType("integer"));
        Assert.assertEquals(66L, jobj.getLong("integer"));
        
        //set double
        jobj.set("float", 666.66);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.FLOAT, jobj.getType("float"));
        Assert.assertEquals(666.66, jobj.getDouble("float"), 0.1e-10);
        
        //set string
        String str = "String";
        jobj.set("string", str);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.STRING, jobj.getType("string"));
        Assert.assertEquals(str, jobj.getString("string"));
        
        str = null;
        jobj.set("string2", str);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("string2"));
        
        //add json
        JsonObject json = new JsonObject();
        jobj.set("object", json);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(json, jobj.get("object"));
        
        json = null;
        jobj.set("object2", json);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("object2"));
        
        //add jsonable
        Jsonable jn  = new Jsonable(){public Json generateJson(){return new JsonArray();}};
        jobj.set("jsonable", jn);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("jsonable"));
        Assert.assertEquals(jn.generateJson(), jobj.get("jsonable"));
        
        jn = null;
        jobj.set("jsonable2", jn);
        Assert.assertEquals(++cnt, jobj.count());
        Assert.assertEquals(JsonType.NULL, jobj.getType("jsonable2"));
        
        //add exception -- name repeat
        boolean error = false;
        try
        {
            jobj.set("null", "NULL:");
        } 
        catch (JsonException e)
        {
            error = true;
        }
        Assert.assertFalse(error);
        Assert.assertEquals(cnt, jobj.count());
    }
    
    @Test
    public void addAll()
    {
        JsonObject jobj = new JsonObject();
        
        //add all json
        HashMap<String, Json> mapJson = new HashMap<String, Json>();
        mapJson.put("object", new JsonObject());
        mapJson.put("array", new JsonArray());
        
        jobj.addAll(mapJson);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(mapJson.get("object"), jobj.get("object"));
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("array"));
        Assert.assertEquals(mapJson.get("array"), jobj.get("array"));
        
        //add all json exception
        Json expJson = mapJson.get("object");
        mapJson.put("object", mapJson.get("array"));
        boolean error = false;
        try
        {
            jobj.addAll(mapJson);
        } 
        catch (JsonException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(expJson, jobj.get("object"));
        
        //add all jsonable
        jobj.clear();
        HashMap<String, Jsonable> mapJsonable = new HashMap<String, Jsonable>();
        mapJsonable.put("object", new Jsonable(){
            public Json generateJson(){return new JsonObject();}
        });
        mapJsonable.put("array", new Jsonable(){
            public Json generateJson(){return new JsonArray();}
        });
        
        jobj.addAllJsonable(mapJsonable);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(mapJsonable.get("object").generateJson(), jobj.get("object"));
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("array"));
        Assert.assertEquals(mapJsonable.get("array").generateJson(), jobj.get("array"));
        
        //add all jsonable exception
        Jsonable expJsonable = mapJsonable.get("object");
        mapJsonable.put("object", mapJsonable.get("array"));
        error = false;
        try
        {
            jobj.addAllJsonable(mapJsonable);
        } 
        catch (JsonException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(expJsonable.generateJson(), jobj.get("object"));
    }
    
    @Test
    public void setAll()
    {
        JsonObject jobj = new JsonObject();
        
        //set all json
        HashMap<String, Json> mapJson = new HashMap<String, Json>();
        mapJson.put("object", new JsonObject());
        mapJson.put("array", new JsonArray());
        
        jobj.setAll(mapJson);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(mapJson.get("object"), jobj.get("object"));
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("array"));
        Assert.assertEquals(mapJson.get("array"), jobj.get("array"));
        
        //add all json repeat
        Json expJson = mapJson.get("array");
        mapJson.put("object", mapJson.get("array"));
        jobj.setAll(mapJson);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("object"));
        Assert.assertEquals(expJson, jobj.get("object"));
        
        //set all jsonable
        jobj.clear();
        HashMap<String, Jsonable> mapJsonable = new HashMap<String, Jsonable>();
        mapJsonable.put("object", new Jsonable(){
            public Json generateJson(){return new JsonObject();}
        });
        mapJsonable.put("array", new Jsonable(){
            public Json generateJson(){return new JsonArray();}
        });
        
        jobj.setAllJsonable(mapJsonable);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.OBJECT, jobj.getType("object"));
        Assert.assertEquals(mapJsonable.get("object").generateJson(), jobj.get("object"));
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("array"));
        Assert.assertEquals(mapJsonable.get("array").generateJson(), jobj.get("array"));
        
        //add all jsonable repeat
        Jsonable expJsonable = mapJsonable.get("array");
        mapJsonable.put("object", mapJsonable.get("array"));
        jobj.setAllJsonable(mapJsonable);
        Assert.assertEquals(2, jobj.count());
        Assert.assertEquals(JsonType.ARRAY, jobj.getType("object"));
        Assert.assertEquals(expJsonable.generateJson(), jobj.get("object"));
    }
    
    @Test
    public void canToXXX() throws IOException, JsonParseException
    {
        String str = "{\"boolean\": true, \"integer\": 30, \"float\": 22.22,"
            + "\"null\": null, \"array\": [], \"object\":{}}";
        JsonObject jobj = (JsonObject)Json.parseJsonText(str);
        jobj.set("booleanStr", "TRUE");
        jobj.set("integerStr", "30");
        jobj.set("floatStr", "2.222E1");
        jobj.set("arrayStr", "[]");
        jobj.set("objectStr", "{}");
        String[] names = {"boolean", "booleanStr", "integer", "integerStr", 
              "float", "floatStr", "null", "array", "arrayStr", "object", "objectStr"};
        
        for(String name: names)
        {
            //boolean
            boolean can = jobj.canToBoolean(name);
            if(name.contains("boolean"))
                Assert.assertTrue(can);
            else
                Assert.assertFalse(can);
            
            //long
            can = jobj.canToLong(name);
            if(name.contains("integer"))
                Assert.assertTrue(can);
            else
                Assert.assertFalse(can);
            
            //double
            can = jobj.canToDouble(name);
            if(name.contains("float") || name.contains("integer"))
                Assert.assertTrue(can);
            else
                Assert.assertFalse(can);
            
            //array
            can = jobj.canToJsonArray(name);
            if(name.equals("array"))
                Assert.assertTrue(can);
            else
                Assert.assertFalse(can);
            
            //object
            can = jobj.canToJsonObject(name);
            if(name.equals("object"))
                Assert.assertTrue(can);
            else
                Assert.assertFalse(can);
        }
    }
    
    @Test
    public void getXXX() throws IOException, JsonParseException
    {
        String str = "{\"boolean\": true, \"integer\": 30, \"float\": 22.22,"
                  + "\"null\": null, \"array\": [], \"object\":{}}";
        JsonObject jobj = (JsonObject)Json.parseJsonText(str);
        jobj.set("booleanStr", "TRUE");
        jobj.set("integerStr", "30");
        jobj.set("floatStr", "2.222E1");
        jobj.set("arrayStr", "[]");
        jobj.set("objectStr", "{}");
        String[] names = {"boolean", "booleanStr", "integer", "integerStr", 
                "float", "floatStr", "null", "array", "arrayStr", "object", "objectStr"};
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("boolean", "true");
        map.put("booleanStr", "TRUE");
        map.put("integer", "30");
        map.put("integerStr", "30");
        map.put("float", "22.22");
        map.put("floatStr", "2.222E1");
        map.put("null", "null");
        map.put("array", "[]");
        map.put("arrayStr", "[]");
        map.put("object", "{}");
        map.put("objectStr", "{}");

        Assert.assertEquals(jobj.get("array"), jobj.getJsonArray("array"));
        Assert.assertEquals(jobj.get("object"), jobj.getJsonObject("object"));
        for(String name: names)
        {
            boolean error = false;
            //getString
            Assert.assertEquals(map.get(name), jobj.getString(name));
             
            //getBoolean
            error = false;
            boolean bval = false;
            try
            {
                bval = jobj.getBoolean(name);
            } 
            catch (JsonException e)
            {
                error = true;
            } 
            if(name.contains("boolean"))
            {
                Assert.assertTrue(bval);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
            
            //getLong
            error = false;
            long lval = 0L;
            try
            {
                lval = jobj.getLong(name);
            } 
            catch (JsonException e)
            {
                error = true;
            } 
            if(name.contains("integer"))
            {
                Assert.assertEquals(30, lval);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
            
            //getDouble
            error = false;
            double dval = 0.0;
            try
            {
                dval = jobj.getDouble(name);
            } 
            catch (JsonException e)
            {
                error = true;
            } 
            if(name.contains("float"))
            {
                Assert.assertEquals(22.22, dval, 0.1e-10);
                Assert.assertFalse(error);
            }
            else if(name.contains("integer"))
            {
                Assert.assertEquals(30.0, dval, 0.1e-10);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(name + ": " + dval + " " + error, error);
            }
            
            //getArray
            error = false;
            JsonArray ary = null;
            try
            {
                ary = jobj.getJsonArray(name);
            } 
            catch (JsonException e)
            {
                error = true;
            } 
            if(name.equals("array"))
            {
                Assert.assertEquals(new JsonArray(), ary);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
            
            //getObject
            error = false;
            JsonObject obj = null;
            try
            {
                obj = jobj.getJsonObject(name);
            } 
            catch (JsonException e)
            {
                error = true;
            } 
            if(name.equals("object"))
            {
                Assert.assertEquals(new JsonObject(), obj);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
        }
    }
}
