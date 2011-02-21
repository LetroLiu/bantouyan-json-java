package com.bantouyan.json.test;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;
import com.bantouyan.json.*;
import com.bantouyan.json.Json.JsonType;

public class TestArray
{
    public static int cnt;
    public static JsonArray ary;
    
    @Before
    public void init() throws IOException, JsonParseException
    {
        String jsonText = "[1, \"10\", 1.1, 1.1E1, \"2.2\", \"2.2E2\", true, \"TRUE\", null, [], {}]";
        ary = (JsonArray)Json.parseJsonText(jsonText);
        cnt = ary.count();
//        for(int i=0; i<cnt; i++) System.out.println(ary.get(i));
    }
    
    public void instance()
    {
        JsonArray jary = new JsonArray();
        Assert.assertTrue(jary.isEmpty());
        Assert.assertEquals(0, ary.count());
        
        ArrayList<Json> jsonList = new ArrayList<Json>();
        jsonList.add(new JsonObject());
        jsonList.add(new JsonArray());
        jary = new JsonArray(jsonList);
        Assert.assertFalse(jary.isEmpty());
        Assert.assertEquals(2, ary.count());
        
        Assert.assertEquals(JsonType.OBJECT, jary.getType(0));
        Assert.assertEquals(jary.getType(0), jary.get(0).getType());
        Assert.assertEquals(JsonType.ARRAY, jary.getType(1));
        Assert.assertEquals(jary.getType(1), jary.get(1).getType());
        
        jary.remove(0);
        Assert.assertEquals(JsonType.ARRAY, jary.getType(0));
        Assert.assertEquals(1, ary.count());
        jary.clear();
        
        Assert.assertTrue(jary.isEmpty());
        Assert.assertEquals(0, ary.count());
        
        jsonList = null;
        jary = new JsonArray(jsonList);
        Assert.assertTrue(jary.isEmpty());
        Assert.assertEquals(0, ary.count());
    }
    
    @Test
    public void canToXXX() throws Exception
    {
        int i;
        // Test canToLong
        Assert.assertTrue(ary.canToLong(0));
        Assert.assertTrue(ary.canToLong(1));
        for(i=2; i<cnt; i++)
            Assert.assertFalse(ary.canToLong(i));
        
        //test canToDouble
        for(i=0; i<cnt; i++)
        {
            if(i<6)
                Assert.assertTrue(ary.canToDouble(i));
            else
                Assert.assertFalse(ary.canToDouble(i));
        }

        //test canToBoolean
        for(i=0; i<cnt; i++)
        {
            if(i==6 || i==7)
                Assert.assertTrue(ary.canToBoolean(i));
            else
                Assert.assertFalse(ary.canToBoolean(i));
        }
        
        //test canToArray
        for(i=0; i<cnt; i++)
        {
            if(i==9)
                Assert.assertTrue(ary.canToJsonArray(i));
            else
                Assert.assertFalse(ary.canToJsonArray(i));
        }
        
        // test cantoObject
        for(i=0; i<cnt; i++)
        {
            if(i==10)
                Assert.assertTrue(ary.canToJsonObject(i));
            else
                Assert.assertFalse(ary.canToJsonObject(i));
        }
    }
    
    @Test
    public void getXXX()
    {
        //test getString
        String[] list = {"1", "10", "1.1", "11.0", "2.2", "2.2E2", "true",
                "TRUE", "null","[]", "{}"};
        for(int i=0; i<cnt; i++)
           Assert.assertEquals(list[i] + " " + ary.getString(i), list[i], ary.getString(i));
        
        //Test getLong
        long longVal = 0L;
        for(int i=0; i<cnt; i++)
        {
            longVal = 0L;
            boolean error = false;
            try
            {
                longVal = ary.getLong(i);
            } 
            catch (JsonException e)
            {
                error = true;
            }
            
            if(i==0)
            {
                Assert.assertEquals(1L, longVal);
                Assert.assertFalse(error);
            }
            else if(i==1)
            {
                Assert.assertEquals(10L, longVal);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
        }
        
        //test getDouble
        double[] dlist = {1, 10, 1.1, 11.0, 2.2, 220.0};
        double dval = 0.0;
        for(int i=0; i<cnt; i++)
        {
            dval = 0.0;
            boolean error = false;
            try
            {
                dval = ary.getDouble(i);
            } 
            catch (JsonException e)
            {
                error = true;
            }
            
            if(i<=5)
            {
                Assert.assertEquals("at [" + i +"] " + dlist[i] + " , " + dval,
                                    dlist[i], dval, 1e-100);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
        }
        
        //test getBoolean
        boolean bval = false;
        for(int i=0; i<cnt; i++)
        {
            bval = false;
            boolean error = false;
            try
            {
                bval = ary.getBoolean(i);
            } 
            catch (JsonException e)
            {
                error = true;
            }
            
            if(i==6 || i==7)
            {
                Assert.assertTrue(bval);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
        }
        
        //test getJsonArray
        JsonArray jaryVal = new JsonArray();
        for(int i=0; i<cnt; i++)
        {
            JsonArray jary = null;
            bval = false;
            boolean error = false;
            try
            {
                jary = ary.getJsonArray(i);
            } 
            catch (JsonException e)
            {
                error = true;
            }
            
            if(i==9)
            {
                Assert.assertEquals(jaryVal, jary);
                Assert.assertEquals(ary.get(i), jary);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
        }
        
        //test getJsonObject
        JsonObject jobjVal = new JsonObject();
        for(int i=0; i<cnt; i++)
        {
            JsonObject jobj = null;
            boolean error = false;
            try
            {
                jobj = ary.getJsonObject(i);
            } 
            catch (JsonException e)
            {
                error = true;
            }
            
            if(i==10)
            {
                Assert.assertEquals(jobjVal, jobj);
                Assert.assertEquals(ary.get(i), jobj);
                Assert.assertFalse(error);
            }
            else
            {
                Assert.assertTrue(error);
            }
        }
        
    }

    @Test
    public void append()
    {
        boolean rst = true;
        ary.clear();
        Assert.assertEquals(0, ary.count());
        
        //append null
        ary.clear();
        rst = ary.append();
        Assert.assertEquals(JsonType.NULL, ary.get(0).getType());
        Assert.assertTrue(rst);
        
        //append boolean
        ary.clear();
        rst = ary.append(true);
        Assert.assertTrue(ary.getBoolean(0));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(0));
        Assert.assertTrue(rst);
        
        //append long
        ary.clear();
        rst = ary.append(10L);
        Assert.assertEquals(10L, ary.getLong(0));
        Assert.assertTrue(rst);
        
        //append double
        ary.clear();
        rst = ary.append(10.3);
        Assert.assertEquals(10.3, ary.getDouble(0), 0.1e-10);
        Assert.assertTrue(rst);
        
        //append String
        ary.clear();
        rst = ary.append("string");
        Assert.assertEquals("string", ary.getString(0));
        Assert.assertTrue(rst);
        String str = null;
        ary.append(str);
        Assert.assertEquals(JsonType.NULL, ary.get(1).getType());
        
        //append Json
        ary.clear();
        Json json = new JsonArray();
        rst = ary.append(json);
        Assert.assertTrue(rst);
        Assert.assertEquals(json, ary.get(0));
        json = null;
        ary.append(json);
        Assert.assertEquals(JsonType.NULL, ary.get(1).getType());
        
        //append Jsonable
        Jsonable jn = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        ary.clear();
        rst = ary.append(jn);
        Assert.assertTrue(rst);
        Assert.assertEquals(JsonType.OBJECT, ary.getType(0));
        
        jn=null;
        boolean error = false;
        try
        {
            ary.append(jn);
        } 
        catch (NullPointerException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
    }

    @Test
    public void addAll() throws Exception
    {
        ArrayList<Json> jsonList = new ArrayList<Json>();
        jsonList.add(new JsonArray());
        jsonList.add(new JsonObject());
        ary.addAll(jsonList);
        Assert.assertEquals(cnt+2, ary.count());
        Assert.assertEquals(JsonType.ARRAY, ary.getType(cnt));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(cnt + 1));
        ary.addAll(1, jsonList);
        Assert.assertEquals(cnt+4, ary.count());
        Assert.assertEquals(JsonType.ARRAY, ary.getType(1));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(2));
        Assert.assertEquals(10, ary.getLong(3));
        Assert.assertEquals(1, ary.getLong(0));
        Assert.assertEquals(JsonType.NULL, ary.get(10).getType());
        
        init();
        ArrayList<Jsonable> jsonableList = new ArrayList<Jsonable>();
        jsonableList.add(new Jsonable(){
            public Json generateJson(){return new JsonArray();}
        });
        jsonableList.add(new Jsonable(){
            public Json generateJson(){return new JsonObject();}
        });
        ary.addAllJsonable(jsonableList);
        Assert.assertEquals(cnt+2, ary.count());
        Assert.assertEquals(JsonType.ARRAY, ary.getType(cnt));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(cnt + 1));
        ary.addAllJsonable(1, jsonableList);
        Assert.assertEquals(cnt+4, ary.count());
        Assert.assertEquals(JsonType.ARRAY, ary.getType(1));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(2));
        Assert.assertEquals(10, ary.getLong(3));
        Assert.assertEquals(1, ary.getLong(0));
        Assert.assertEquals(JsonType.NULL, ary.getType(10));
    }
    
    @Test
    public void insert()
    {
        //insert null 
        ary.insert(0);
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        
        //insert boolean
        ary.insert(0, true);
        Assert.assertTrue(ary.getBoolean(0));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(0));
        
        //insert double
        ary.insert(0, 3.33);
        Assert.assertEquals(JsonType.FLOAT, ary.getType(0));
        Assert.assertEquals(3.33, ary.getDouble(0), 0.1e-10);
        
        //insert long
        ary.insert(0, 30);
        Assert.assertEquals(30, ary.getLong(0));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(0));
        
        //insert string
        String str = "string";
        ary.insert(0, str);
        Assert.assertEquals(JsonType.STRING, ary.get(0).getType());
        Assert.assertEquals(str, ary.getString(0));
        str = null;
        ary.insert(0, str);
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        
        //insert Json
        Json json = new JsonObject();
        ary.insert(0, json);
        Assert.assertEquals(JsonType.OBJECT, ary.getType(0));
        json = null;
        ary.insert(0, json);
        Assert.assertEquals(JsonType.NULL, ary.get(0).getType());

        //insert Jsonable
        Jsonable jn = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        ary.insert(0, jn);
        Assert.assertEquals(JsonType.OBJECT, ary.getType(0));
        
        jn=null;
        boolean error = false;
        try
        {
            ary.insert(0, jn);
        } 
        catch (NullPointerException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
    }
    
    @Test
    public void set()
    {
        //set null 
        ary.set(0);
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        
        //set boolean
        ary.set(0, true);
        Assert.assertTrue(ary.getBoolean(0));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(0));
        
        //set double
        ary.set(0, 3.33);
        Assert.assertEquals(JsonType.FLOAT, ary.getType(0));
        Assert.assertEquals(3.33, ary.getDouble(0), 0.1e-10);
        
        //set long
        ary.set(0, 30);
        Assert.assertEquals(30, ary.getLong(0));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(0));
        
        //set string
        String str = "string";
        ary.set(0, str);
        Assert.assertEquals(JsonType.STRING, ary.get(0).getType());
        Assert.assertEquals(str, ary.getString(0));
        str = null;
        ary.set(0, str);
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        
        //set Json
        Json json = new JsonObject();
        ary.set(0, json);
        Assert.assertEquals(JsonType.OBJECT, ary.getType(0));
        json = null;
        ary.set(0, json);
        Assert.assertEquals(JsonType.NULL, ary.get(0).getType());

        //set Jsonable
        Jsonable jn = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        ary.set(0, jn);
        Assert.assertEquals(JsonType.OBJECT, ary.getType(0));
        
        jn=null;
        boolean error = false;
        try
        {
            ary.insert(0, jn);
        } 
        catch (NullPointerException e)
        {
            error = true;
        }
        Assert.assertTrue(error);
    }
    
    
}
