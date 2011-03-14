package com.bantouyan.json.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.bantouyan.json.*;
import com.bantouyan.json.Json.JsonType;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Ignore;

public class TestJsonObject
{
    @Test
    public void jsonObject_new_isEmpty_count_getType_equals_sets_getString_clone_remove()
    {
        JsonObject sobj = new JsonObject();
        JsonArray ary = new JsonArray();
        sobj.add("ary", ary);

        JsonObject obj = new JsonObject();
        Assert.assertTrue(obj.isEmpty());
        Assert.assertEquals(0, obj.count());
        
        obj.add("nullName");
        obj.add("booleanName", true);
        obj.add("integerName", 20);
        obj.add("floatName", 33.33);
        obj.add("stringName", "string");
        obj.add("arrayName", ary);
        obj.add("objectName", sobj);
        
        HashMap<String, Json> map = new HashMap<String, Json>();
        Set<String> names = obj.nameSet();
        Assert.assertEquals(7, names.size());
        for(String name: names)
        {
            map.put(name, obj.get(name));
        }
        JsonObject obj2 = new JsonObject(map);
        
        Assert.assertFalse(obj2.isEmpty());
        Assert.assertEquals(7, obj2.count());
        Assert.assertEquals(obj, obj2);
        
        JsonObject obj3 = obj2.clone();
        
        Assert.assertEquals(JsonType.OBJECT, obj.getType());
        Assert.assertEquals(JsonType.OBJECT, obj2.getType());
        Collection<Json> values = obj2.values();
        Assert.assertEquals(7, values.size());
        
        Set<Entry<String, Json>> entrySet = obj2.entrySet();
        Assert.assertEquals(7, entrySet.size());
        for(Entry<String, Json> entry: entrySet)
        {
            Assert.assertEquals(entry.getValue(), obj2.get(entry.getKey()));
        }
        
        Assert.assertEquals(null, obj.get(null));
        Assert.assertEquals(null, obj.get("noSuchName"));

        Assert.assertEquals("null", obj.getString("nullName"));
        Assert.assertEquals("true", obj.getString("booleanName"));
        Assert.assertEquals("20", obj.getString("integerName"));
        Assert.assertEquals("33.33", obj.getString("floatName"));
        Assert.assertEquals("string", obj.getString("stringName"));
        Assert.assertEquals("[]", obj.getString("arrayName"));
        Assert.assertEquals("{\"ary\":[]}", obj.getString("objectName"));

        Assert.assertEquals(obj2, obj3);
        Assert.assertFalse(obj2 == obj3);
        for(String name: obj2.nameSet())
        {
            Json j2 = obj2.get(name);
            Json j3 = obj3.get(name);
            Assert.assertEquals(j2, j3);
            if(j2 instanceof JsonPrimitive)
                Assert.assertTrue(j2 == j3);
            else
                Assert.assertFalse(j2 == j3);
        }
        
        Assert.assertEquals(null, obj.get(null));
        Assert.assertEquals(null, obj.get("NoSuchName"));
        Assert.assertEquals(null, obj.getString(null));
        Assert.assertEquals(null, obj.getString("NoSuchName"));
        
        obj = new JsonObject();
        obj.add("a", "A");
        obj.add("b", "B");
        Assert.assertTrue(obj.containsName("a"));
        Assert.assertTrue(obj.containsName("b"));
        obj.remove("a");
        Assert.assertFalse(obj.containsName("a"));
        Assert.assertTrue(obj.containsName("b"));
    }
    
    @Test
    public void jsonObject_newMap()
    {
        // invalid name
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(new Date(), "Date");
        try
        {
            JsonObject json = new JsonObject(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
        }
        
        try
        {
            JsonObject json = new JsonObject(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
        }
        
        //invalid value
        map = new HashMap<Object, Object>();
        map.put("Date", new Date());
        try
        {
            JsonObject json = new JsonObject(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        try
        {
            JsonObject json = new JsonObject(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        //circle
        map = new HashMap<Object, Object>();
        map.put("self", map);
        try
        {
            JsonObject json = new JsonObject(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Map."));
        }
        
        try
        {
            JsonObject json = new JsonObject(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Map."));
        }
        
        //normal
        HashMap<Object, Object> map2 = new HashMap<Object, Object>();
        ArrayList<Object> list = new ArrayList<Object>();
        map2.put("NULL", null);
        map2.put("INTEGER", 356);
        map2.put("FLOAT", 34.1e2);
        map2.put("BOOLEAN", true);
        map2.put("STRING", "string");
        map2.put("ARRAY", list);
        map2.put("OBJECT", new HashMap<Object, Object>());
        list.add(false);
        list.add(null);
        JsonObject json = new JsonObject(map2);
        System.out.println("===================================");
        String jsonStr = json.generateJsonText();
        System.out.println(jsonStr);
        Assert.assertEquals(7, json.count());
        Assert.assertEquals(JsonType.NULL, json.getType("NULL"));
        Assert.assertEquals(JsonType.INTEGER, json.getType("INTEGER"));
        Assert.assertEquals(356, json.getLong("INTEGER"));
        Assert.assertEquals(JsonType.FLOAT, json.getType("FLOAT"));
        Assert.assertEquals(3410.0, json.getDouble("FLOAT"), 0.1e-9);
        Assert.assertEquals(JsonType.BOOLEAN, json.getType("BOOLEAN"));
        Assert.assertEquals(true, json.getBoolean("BOOLEAN"));
        Assert.assertEquals(JsonType.STRING, json.getType("STRING"));
        Assert.assertEquals("string", json.getString("STRING"));
        Assert.assertEquals(JsonType.ARRAY, json.getType("ARRAY"));
        Assert.assertEquals(JsonType.OBJECT, json.getType("OBJECT"));
        
        //JsonParser
        ArrayList<Object> list2 = new ArrayList<Object>();
        list2.add("v1");
        list2.add("&&v2");
        HashMap<Object, Object> map3 = new HashMap<Object, Object>();
        map3.put("**name", "v1");
        map3.put("num", "&&30");
        map3.put("list", list2);
        
        JsonObject jobj = new JsonObject(map3, parser);
        Assert.assertTrue(jobj.containsName("name"));
        Assert.assertFalse(jobj.containsName("**name"));
        Assert.assertEquals("30", jobj.getString("num"));
        Assert.assertEquals(3, jobj.count());
    }
    
    @Test
    public void booleanElement_get_canTo()
    {
        System.out.println("==================================");
        JsonObject obj = new JsonObject();
        JsonObject sobj = new JsonObject();
        JsonArray ary = new JsonArray();
        sobj.add("ary", ary);
        
        obj.add("nullName");
        obj.add("booleanName", true);
        obj.add("integerName", 20);
        obj.add("floatName", 33.33);
        obj.add("stringName", "string");
        obj.add("arrayName", ary);
        obj.add("objectName", sobj);
        
        Set<String> names = obj.nameSet();
        HashSet<String> hnames = new HashSet<String>(names);
        hnames.add("noSuchName");
        hnames.add(null);
        for(String name: hnames)
        {
            boolean fail = false;
            boolean val = false;
            try
            {
                val = obj.getBoolean(name);
            } 
            catch (JsonException e)
            {
                fail = true;
                String msg = e.getMessage();
                System.out.println(msg);
                String expStr = "Cannot transfer element corresponding " + name + " to boolean value.";
                Assert.assertEquals(expStr, msg);
            }
            if(name != null && name.equals("booleanName"))
            {
                Assert.assertEquals(true, val);
                
                Assert.assertFalse(fail);
            }
            else
            {
                Assert.assertTrue(fail);
            }
        }
    }
    
    @Test
    public void longElement_get_canTo()
    {
        System.out.println("==================================");
        JsonObject obj = new JsonObject();
        JsonObject sobj = new JsonObject();
        JsonArray ary = new JsonArray();
        sobj.add("ary", ary);
        
        obj.add("nullName");
        obj.add("booleanName", true);
        obj.add("integerName", 20);
        obj.add("integerStringA", " 20 ");
        obj.add("integerStringB", " +20");
        obj.add("integerStringC", " -20");
        obj.add("invalidIntegerStringD", " - 20");
        obj.add("invalidIntegerStringE", " +20L");
        obj.add("invalidIntegerStringF", " 20 3");
        obj.add("invalidIntegerStringG", " s 20");
        obj.add("floatName", 33.33);
        obj.add("stringName", "string");
        obj.add("arrayName", ary);
        obj.add("objectName", sobj);
        
        Set<String> names = obj.nameSet();
        HashSet<String> hnames = new HashSet<String>(names);
        hnames.add("noSuchName");
        hnames.add(null);
        for(String name: hnames)
        {
            boolean fail = false;
            long val = 0L;
            try
            {
                val = obj.getLong(name);
            } 
            catch (JsonException e)
            {
                fail = true;
                String msg = e.getMessage();
                System.out.println(msg);
                String expStr = "Cannot transfer element corresponding " + name + " to long value.";
                Assert.assertEquals(expStr, msg);
            }
            if(name != null && name.startsWith("integer"))
            {
                if(name.equals("integerStringC"))
                    Assert.assertEquals(-20, val);
                else
                    Assert.assertEquals(20, val);
                
                Assert.assertFalse(fail);
            }
            else
            {
                Assert.assertTrue(fail);
            }
        }
    }
    
    @Test
    public void doubleElement_get_canTo()
    {
        System.out.println("==================================");
        JsonObject obj = new JsonObject();
        JsonObject sobj = new JsonObject();
        JsonArray ary = new JsonArray();
        sobj.add("ary", ary);
        
        obj.add("nullName");
        obj.add("booleanName", true);
        obj.add("integerName", 20);
        obj.add("integerStringA", " 20 ");
        obj.add("integerStringB", " +20");
        obj.add("integerStringC", " -20");
        obj.add("invalidIntegerStringD", " - 20");
        obj.add("invalidIntegerStringE", " +20L");
        obj.add("invalidIntegerStringF", " 20 3");
        obj.add("invalidIntegerStringG", " s 20");
        obj.add("floatName", 33.33);
        obj.add("floatStringA", " +33.33 ");
        obj.add("floatStringB", " 33.33 ");
        obj.add("floatStringC", " +3.333e1 ");
        obj.add("floatStringD", " 3.333e+1 ");
        obj.add("floatStringE", " +3.333e+1 ");
        obj.add("floatStringF", " 3.333e1 ");
        obj.add("floatStringG", " 333.3e-1 ");
        obj.add("floatStringMA", " -33.33 ");
        obj.add("floatStringMC", " -3.333e1 ");
        obj.add("floatStringMD", " -3.333e+1 ");
        obj.add("floatStringME", " -333.3e-1 ");
        obj.add("invlaidFloatStringA", " + 33.33 ");
        obj.add("invlaidFloatStringB", " 33.33s ");
        obj.add("invlaidFloatStringC", " +3.333e 1 ");
        obj.add("invlaidFloatStringD", " 3.333 e+1 ");
        obj.add("invlaidFloatStringE", " +3.333e+ 1 ");
        obj.add("invlaidFloatStringF", " 3.333 e 1 ");
        obj.add("invlaidFloatStringG", " 333.3e-1 l");
        obj.add("invlaidFloatStringMA", " - 33.33 ");
        obj.add("invlaidFloatStringMC", " -3. 333e1 ");
        obj.add("invlaidFloatStringMD", " -3.333e+1 2 ");
        obj.add("invlaidFloatStringME", " + 333.3e-1 ");
        obj.add("stringName", "string");
        obj.add("arrayName", ary);
        obj.add("objectName", sobj);
        
        Set<String> names = obj.nameSet();
        HashSet<String> hnames = new HashSet<String>(names);
        hnames.add("noSuchName");
        hnames.add(null);
        for(String name: hnames)
        {
            boolean fail = false;
            double val = 0.0;
            try
            {
                val = obj.getDouble(name);
            } 
            catch (JsonException e)
            {
                fail = true;
                String msg = e.getMessage();
                System.out.println(msg);
                String expStr = "Cannot transfer element corresponding " + name + " to double value.";
                Assert.assertEquals(expStr, msg);
            }
            if(name != null && (name.startsWith("integer") || name.startsWith("float")))
            {
                System.out.println(name + ": " + val);
                if(name.equals("integerStringC")) val = -val;
                if(name.startsWith("integer"))
                    Assert.assertEquals(20.0, val, 0.1e-10);
                else if(name.startsWith("floatStringM"))
                    Assert.assertEquals(-33.33, val, 0.1e-10);
                else
                    Assert.assertEquals(33.33, val, 0.1e-10);

                Assert.assertFalse(fail);
            }
            else
            {
                Assert.assertTrue(fail);
            }
        }
    }
    
    @Test
    public void set_add()
    {
        System.out.println("==================================");
        JsonObject sobj = new JsonObject();
        JsonObject nobj = null;
        JsonArray ary = new JsonArray();
        JsonArray nary = null;
        sobj.add("ary", ary);
        String str = "hello \n world";
        String nstr = null;
        Jsonable ableo = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        Jsonable ablen = new Jsonable(){public Json generateJson(){return null;}};
        Jsonable nable = null;
        
        JsonObject obj = new JsonObject();
        Assert.assertTrue(obj.isEmpty());
        
        //set null name
        obj.set(null);
        obj.set(null, false);
        obj.set(null, 30);
        obj.set(null, 33.33);
        obj.set(null, str);
        obj.set(null, ary);
        obj.set(null, sobj);
        obj.set(null, ableo);
        Assert.assertTrue(obj.isEmpty());
        
        //add null name
        String msg = "Element Name in JsonObject cannot be null.";
        int errNum = 0;
        try{ obj.add(null);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, false);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, 30);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, 33.33);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, str);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, ary);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, sobj);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{ obj.add(null, ableo);} 
        catch (JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        Assert.assertTrue(obj.isEmpty());  
        Assert.assertEquals(8, errNum);
        
        //add exists name
        obj.set("name", "str");
        errNum = 0;
        Assert.assertEquals(1, obj.count());
        msg = "Name \"name\" already exist in this JsonObject.";
        try{obj.add("name");}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", true);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", 99);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", 88.88);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", str);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", ary);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", sobj);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        try{obj.add("name", ableo);}
        catch(JsonException e)
        {Assert.assertEquals(msg, e.getMessage()); errNum++;}
        Assert.assertEquals(1, obj.count());
        Assert.assertEquals(8, errNum);
        
        //set exists name
        obj.set("name");
        Assert.assertEquals(JsonType.NULL, obj.getType("name"));
        obj.set("name", false);
        Assert.assertEquals(JsonType.BOOLEAN, obj.getType("name"));
        Assert.assertFalse(obj.getBoolean("name"));
        obj.set("name", 88);
        Assert.assertEquals(JsonType.INTEGER, obj.getType("name"));
        Assert.assertEquals(88, obj.getLong("name"));
        obj.set("name", 99.99);
        Assert.assertEquals(JsonType.FLOAT, obj.getType("name"));
        Assert.assertEquals(99.99, obj.getDouble("name"), 0.1e-10);
        obj.set("name", str);
        Assert.assertEquals(JsonType.STRING, obj.getType("name"));
        Assert.assertEquals(str, obj.getString("name"));
        obj.set("name", nstr);
        Assert.assertEquals(JsonType.NULL, obj.getType("name"));
        obj.set("name", ary);
        Assert.assertEquals(JsonType.ARRAY, obj.getType("name"));
        Assert.assertEquals(ary, obj.getJsonArray("name"));
        obj.set("name", nary);
        Assert.assertEquals(JsonType.NULL, obj.getType("name"));
        obj.set("name", sobj);
        Assert.assertEquals(JsonType.OBJECT, obj.getType("name"));
        Assert.assertEquals(sobj, obj.getJsonObject("name"));
        obj.set("name", nobj);
        Assert.assertEquals(JsonType.NULL, obj.getType("name"));
        obj.set("name", ableo);
        Assert.assertEquals(JsonType.OBJECT, obj.getType("name"));
        Assert.assertEquals(ableo.generateJson(), obj.getJsonObject("name"));
        obj.set("name", ablen);
        Assert.assertEquals(JsonType.NULL, obj.getType("name"));
        obj.set("name", nable);
        Assert.assertEquals(JsonType.NULL, obj.getType("name"));
        Assert.assertEquals(1, obj.count());
        obj.clear();
        
        // set new name
        obj.set("nn");
        Assert.assertEquals(JsonType.NULL, obj.getType("nn"));
        obj.set("nt", true);
        Assert.assertEquals(JsonType.BOOLEAN, obj.getType("nt"));
        Assert.assertTrue(obj.getBoolean("nt"));
        obj.set("ni", 99);
        Assert.assertEquals(JsonType.INTEGER, obj.getType("ni"));
        Assert.assertEquals(99, obj.getLong("ni"));
        obj.set("nf", 99.0);
        Assert.assertEquals(JsonType.FLOAT, obj.getType("nf"));
        Assert.assertEquals(99.0, obj.getDouble("nf"), 0.1e-10);
        obj.set("ns", str);
        Assert.assertEquals(JsonType.STRING, obj.getType("ns"));
        Assert.assertEquals(str, obj.getString("ns"));
        obj.set("nsn", nstr);
        Assert.assertEquals(JsonType.NULL, obj.getType("nsn"));
        obj.set("nary", ary);
        Assert.assertEquals(JsonType.ARRAY, obj.getType("nary"));
        Assert.assertEquals(ary, obj.getJsonArray("nary"));
        obj.set("naryn", nary);
        Assert.assertEquals(JsonType.NULL, obj.getType("naryn"));
        obj.set("nobj", sobj);
        Assert.assertEquals(JsonType.OBJECT, obj.getType("nobj"));
        Assert.assertEquals(sobj, obj.getJsonObject("nobj"));
        obj.set("nobjn", nobj);
        Assert.assertEquals(JsonType.NULL, obj.getType("nobjn"));
        obj.set("nableo", ableo);
        Assert.assertEquals(JsonType.OBJECT, obj.getType("nableo"));
        Assert.assertEquals(ableo.generateJson(), obj.getJsonObject("nableo"));
        obj.set("nablen", ablen);
        Assert.assertEquals(JsonType.NULL, obj.getType("nablen"));
        obj.set("nnable", nable);
        Assert.assertEquals(JsonType.NULL, obj.getType("nnable"));
        Assert.assertEquals(13, obj.count());

        
        // add new name
        obj.clear();
        Assert.assertEquals(0, obj.count());
        obj.add("nn");
        Assert.assertEquals(JsonType.NULL, obj.getType("nn"));
        obj.add("nt", true);
        Assert.assertEquals(JsonType.BOOLEAN, obj.getType("nt"));
        Assert.assertTrue(obj.getBoolean("nt"));
        obj.add("ni", 99);
        Assert.assertEquals(JsonType.INTEGER, obj.getType("ni"));
        Assert.assertEquals(99, obj.getLong("ni"));
        obj.add("nf", 99.0);
        Assert.assertEquals(JsonType.FLOAT, obj.getType("nf"));
        Assert.assertEquals(99.0, obj.getDouble("nf"), 0.1e-10);
        obj.add("ns", str);
        Assert.assertEquals(JsonType.STRING, obj.getType("ns"));
        Assert.assertEquals(str, obj.getString("ns"));
        obj.add("nsn", nstr);
        Assert.assertEquals(JsonType.NULL, obj.getType("nsn"));
        obj.add("nary", ary);
        Assert.assertEquals(JsonType.ARRAY, obj.getType("nary"));
        Assert.assertEquals(ary, obj.getJsonArray("nary"));
        obj.add("naryn", nary);
        Assert.assertEquals(JsonType.NULL, obj.getType("naryn"));
        obj.add("nobj", sobj);
        Assert.assertEquals(JsonType.OBJECT, obj.getType("nobj"));
        Assert.assertEquals(sobj, obj.getJsonObject("nobj"));
        obj.add("nobjn", nobj);
        Assert.assertEquals(JsonType.NULL, obj.getType("nobjn"));
        obj.add("nableo", ableo);
        Assert.assertEquals(JsonType.OBJECT, obj.getType("nableo"));
        Assert.assertEquals(ableo.generateJson(), obj.getJsonObject("nableo"));
        obj.add("nablen", ablen);
        Assert.assertEquals(JsonType.NULL, obj.getType("nablen"));
        obj.add("nnable", nable);
        Assert.assertEquals(JsonType.NULL, obj.getType("nnable"));
        Assert.assertEquals(13, obj.count());
    }
    
    @Test
    public void addAll_setAll_exception()
    {
        JsonObject obj = new JsonObject();
        
        // invalid name
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(new Date(), "Date");
        try
        {
            obj.addAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
        }
        
        try
        {
            obj.addAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
        }
        
        try
        {
            obj.setAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
        }
        
        try
        {
            obj.setAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Map key cannot cast to string."));
        }
        
        //invalid value
        map = new HashMap<Object, Object>();
        map.put("Date", new Date());
        try
        {
            obj.addAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        try
        {
            obj.addAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }

        try
        {
            obj.setAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        try
        {
            obj.setAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        //circle
        map = new HashMap<Object, Object>();
        map.put("self", map);
        try
        {
            obj.addAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Map."));
        }
        
        try
        {
            obj.addAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Map."));
        } 
        
        try
        {
            obj.setAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Map."));
        }
        
        try
        {
            obj.setAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Map."));
        } 
        
        //null name
        map = new HashMap<Object, Object>();
        map.put(null, "NULL");
        try
        {
            obj.addAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Try to add element with name is null."));
        } 
        
        try
        {
            obj.addAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Try to add element with name is null."));
        } 
        
        //repeat name
        obj.add("nameA", "valueA");
        obj.add("nameB", "valueB");
        map.clear();
        map.put("nameA", "valueA_map");
        map.put("nameB", "valueB_map");
        try
        {
            obj.addAll(map);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.startsWith("Try to add exists names"));
        }

        try
        {
            obj.addAll(map, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.startsWith("Try to add exists names"));
        }
        
        JsonObject obj2 = new JsonObject(map);
        try
        {
            obj.addAll(obj2);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.startsWith("Try to add exists names"));
        }
    }
    
    @Test
    public void addAll_setAll_normal()
    {
        System.out.println("==================================");
        Jsonable jableObj = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        Jsonable jableNull = new Jsonable(){public Json generateJson(){return new JsonPrimitive();}};
        JsonArray empAry = new JsonArray();
        JsonObject empObj = new JsonObject();
        JsonObject sobj = new JsonObject();
        sobj.set("NULL");
        sobj.set("BOOLEAN", false);
        sobj.set("INTEGER", 33);
        sobj.set("FLOAT", 33.33);
        sobj.set("STRING", "string");
        sobj.set("OBJECT", new JsonObject());
        sobj.set("ARRAY", new JsonArray());
        sobj.set("NULL_able", jableNull);
        sobj.set("OBJECT_able", jableObj);
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("NULL", null);
        map.put("BOOLEAN", false);
        map.put("INTEGER", 33);
        map.put("FLOAT", 33.33);
        map.put("STRING", "string");
        map.put("OBJECT", new JsonObject());
        map.put("ARRAY", new JsonArray());
        map.put("NULL_able", jableNull);
        map.put("OBJECT_able", jableObj);
        
        //JsonObject
        JsonObject obj = new JsonObject();
        obj.addAll(sobj);
        Assert.assertEquals(sobj, obj);
        
        obj.clear();
        obj.set("BOOLEAN", "str");
        obj.set("ARRAY", 356);
        obj.set("other");
        obj.setAll(sobj);
        sobj.set("other");
        Assert.assertEquals(sobj, obj);
        
        //Map
        obj.clear();
        obj.addAll(map);
        sobj.remove("other");
        Assert.assertEquals(sobj, obj);
        
        obj.clear();
        obj.set("BOOLEAN", "str");
        obj.set("ARRAY", 356);
        obj.set("other");
        sobj.set("other");
        obj.setAll(map);
        Assert.assertEquals(sobj, obj);
        
        // for parser
        ArrayList<Object> list = new ArrayList<Object>();
        list.add("v1");
        list.add("&&v2");
        HashMap map2 = new HashMap<Object, Object>();
        map2.put("**name", "v1");
        map2.put("num", "&&30");
        map2.put("list", list);
        
        //JsonParser
        obj.clear();
        obj.addAll(map2, parser);
        Assert.assertTrue(obj.containsName("name"));
        Assert.assertFalse(obj.containsName("**name"));
        Assert.assertEquals("30", obj.getString("num"));
        
        obj.clear();
        obj.set("num", 999);
        obj.setAll(map2, parser);
        Assert.assertTrue(obj.containsName("name"));
        Assert.assertFalse(obj.containsName("**name"));
        Assert.assertEquals("30", obj.getString("num"));
    }
    
    @Test
    public void double_NaN_Infinity()
    {
        double nan = Double.NaN;
        double posInfinity = Double.POSITIVE_INFINITY;
        double negInfinity = Double.NEGATIVE_INFINITY;
        
        JsonObject obj = new JsonObject();
        obj.set("nan", nan);
        obj.set("pi", posInfinity);
        obj.set("ni", negInfinity);
        
        Assert.assertEquals(JsonType.STRING, obj.getType("nan"));
        Assert.assertEquals(JsonType.STRING, obj.getType("pi"));
        Assert.assertEquals(JsonType.STRING, obj.getType("ni"));
        Assert.assertEquals("NaN", obj.getString("nan"));
        Assert.assertEquals("Infinity", obj.getString("pi"));
        Assert.assertEquals("-Infinity", obj.getString("ni"));
        
        obj.clear();
        obj.add("nan", nan);
        obj.add("pi", posInfinity);
        obj.add("ni", negInfinity);
        
        Assert.assertEquals(JsonType.STRING, obj.getType("nan"));
        Assert.assertEquals(JsonType.STRING, obj.getType("pi"));
        Assert.assertEquals(JsonType.STRING, obj.getType("ni"));
        Assert.assertEquals("NaN", obj.getString("nan"));
        Assert.assertEquals("Infinity", obj.getString("pi"));
        Assert.assertEquals("-Infinity", obj.getString("ni"));
    }

    public static JsonParser parser = new JsonParser(){
        
        @Override
        public boolean canToJson(Object obj)
        {
            if(obj instanceof String && ((String)obj).startsWith("&&"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        @Override
        public Json changeToJson(Object obj) throws JsonException
        {
            if(obj instanceof String && ((String)obj).startsWith("&&"))
            {
                return new JsonPrimitive(((String)obj).substring(2));
            }
            else
            {
                throw new JsonException();
            }
        }
        
        @Override
        public boolean canToName(Object obj)
        {
            if(obj instanceof String && ((String)obj).startsWith("**"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        @Override
        public String changeToName(Object obj) throws JsonException
        {
            if(obj instanceof String && ((String)obj).startsWith("**"))
            {
                return ((String)obj).substring(2);
            }
            else
            {
                throw new JsonException();
            }
        }
    };
}
