package com.bantouyan.json.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.bantouyan.json.*;
import com.bantouyan.json.Json.JsonType;

import org.junit.Test;
import org.junit.Assert;

public class TestObject2
{
    @Test
    public void jsonObject_new_isEmpty_count_getType_equals_sets_getString_clone()
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
    public void addAll_setALL()
    {
        System.out.println("==================================");
        JsonObject sobj = new JsonObject();
        sobj.set("str", "string");
        sobj.set("int", 30);
        sobj.set("float", 33.33);
        sobj.set("bool", false);
        sobj.set("nullj");
        JsonArray ary = new JsonArray();
        HashMap<String, Json> jsonMap = new HashMap<String, Json>();
        jsonMap.put("nullp", null);
        jsonMap.put("nullj", sobj.get("nullj"));
        jsonMap.put("str", sobj.get("str"));
        jsonMap.put("int", sobj.get("int"));
        jsonMap.put("float", sobj.get("float"));
        jsonMap.put("bool", sobj.get("bool"));
        jsonMap.put("object", sobj);
        jsonMap.put("array", ary);
        
        Jsonable ableo = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        Jsonable ablen = new Jsonable(){public Json generateJson(){return null;}};
        HashMap<String, Jsonable> ableMap = new HashMap<String, Jsonable>();
        ableMap.put("anullp", null);
        ableMap.put("aobjo", ableo);
        ableMap.put("anullj", ablen);
        
        JsonObject obj = new JsonObject();
        Assert.assertEquals(0, obj.count());
        
        //test addAll exception
        jsonMap.put(null, null);
        obj.set("bool");
        obj.set("array");
        boolean error = false;
        try
        {
            obj.addAll(jsonMap);
        } 
        catch (JsonException e)
        {
            String errStr = "Try to add element with name is null and exists names \"bool, array\" to this JsonObject.";
            System.out.println(e.getMessage());
            Assert.assertEquals(errStr, e.getMessage());
            error = true;
        }
        Assert.assertTrue(error);
        
        jsonMap.remove(null);
        error = false;
        try
        {
            obj.addAll(jsonMap);
        } 
        catch (JsonException e)
        {
            String errStr = "Try to add exists names \"bool, array\" to this JsonObject.";
            System.out.println(e.getMessage());
            Assert.assertEquals(errStr, e.getMessage());
            error = true;
        }
        Assert.assertTrue(error);
        
        jsonMap.put(null, null);
        obj.clear();
        error = false;
        try
        {
            obj.addAll(jsonMap);
        } 
        catch (JsonException e)
        {
            String errStr = "Try to add element with name is null to this JsonObject.";
            System.out.println(e.getMessage());
            Assert.assertEquals(errStr, e.getMessage());
            error = true;
        }
        Assert.assertTrue(error);
        jsonMap.remove(null);
        
        //test addAllJsonalbe exception
        ableMap.put(null, null);
        obj.set("anullj");
        obj.set("anullp");
        error = false;
        try
        {
            obj.addAllJsonable(ableMap);
        } 
        catch (JsonException e)
        {
            String errStr = "Try to add element with name is null and exists names \"anullj, anullp\" to this JsonObject.";
            System.out.println(e.getMessage());
            Assert.assertEquals(errStr, e.getMessage());
            error = true;
        }
        Assert.assertTrue(error);
        
        ableMap.remove(null);
        error = false;
        try
        {
            obj.addAllJsonable(ableMap);
        } 
        catch (JsonException e)
        {
            String errStr = "Try to add exists names \"anullj, anullp\" to this JsonObject.";
            System.out.println(e.getMessage());
            Assert.assertEquals(errStr, e.getMessage());
            error = true;
        }
        Assert.assertTrue(error);
        
        ableMap.put(null, null);
        obj.clear();
        error = false;
        try
        {
            obj.addAllJsonable(ableMap);
        } 
        catch (JsonException e)
        {
            String errStr = "Try to add element with name is null to this JsonObject.";
            System.out.println(e.getMessage());
            Assert.assertEquals(errStr, e.getMessage());
            error = true;
        }
        Assert.assertTrue(error);
        ableMap.remove(null);
        
        //test addAll addAllJsonable
        obj.clear();
        obj.addAll(jsonMap);
        obj.addAllJsonable(ableMap);
        
        Assert.assertEquals(11, obj.count());
        Assert.assertEquals(JsonType.NULL, obj.getType("nullp"));
        Assert.assertEquals(JsonType.NULL, obj.getType("nullj"));
        Assert.assertEquals(JsonType.STRING, obj.getType("str"));
        Assert.assertEquals("string", obj.getString("str"));
        Assert.assertEquals(JsonType.INTEGER, obj.getType("int"));
        Assert.assertEquals(30, obj.getLong("int"));
        Assert.assertEquals(JsonType.FLOAT, obj.getType("float"));
        Assert.assertEquals(33.33, obj.getDouble("float"), 0.1e-10);
        Assert.assertEquals(JsonType.BOOLEAN, obj.getType("bool"));
        Assert.assertEquals(false, obj.getBoolean("bool"));
        Assert.assertEquals(JsonType.OBJECT, obj.getType("object"));
        Assert.assertEquals(sobj, obj.getJsonObject("object"));
        Assert.assertEquals(JsonType.ARRAY, obj.getType("array"));
        Assert.assertEquals(ary, obj.getJsonArray("array"));
        Assert.assertEquals(JsonType.NULL, obj.getType("anullp"));
        Assert.assertEquals(JsonType.OBJECT, obj.getType("aobjo"));
        Assert.assertEquals(ableo.generateJson(), obj.getJsonObject("aobjo"));
        Assert.assertEquals(JsonType.NULL, obj.getType("anullj"));
        
        
        //test setAll setAllJsonable
        obj.set("nullp", 3);
        obj.set("nullj", 23);
        obj.set("str");
        obj.set("int", false);
        obj.set("float", "string");
        obj.set("bool", 96);
        obj.set("object", "string");
        obj.set("array", true);
        obj.set("anullp", sobj);
        obj.set("aobjo", 88);
        obj.set("anullj", ary);
        
        jsonMap.put(null, ary);
        ableMap.put(null, null);
        obj.setAll(jsonMap);
        obj.setAllJsonable(ableMap);
        
        Assert.assertEquals(11, obj.count());
        Assert.assertEquals(JsonType.NULL, obj.getType("nullp"));
        Assert.assertEquals(JsonType.NULL, obj.getType("nullj"));
        Assert.assertEquals(JsonType.STRING, obj.getType("str"));
        Assert.assertEquals("string", obj.getString("str"));
        Assert.assertEquals(JsonType.INTEGER, obj.getType("int"));
        Assert.assertEquals(30, obj.getLong("int"));
        Assert.assertEquals(JsonType.FLOAT, obj.getType("float"));
        Assert.assertEquals(33.33, obj.getDouble("float"), 0.1e-10);
        Assert.assertEquals(JsonType.BOOLEAN, obj.getType("bool"));
        Assert.assertEquals(false, obj.getBoolean("bool"));
        Assert.assertEquals(JsonType.OBJECT, obj.getType("object"));
        Assert.assertEquals(sobj, obj.getJsonObject("object"));
        Assert.assertEquals(JsonType.ARRAY, obj.getType("array"));
        Assert.assertEquals(ary, obj.getJsonArray("array"));
        Assert.assertEquals(JsonType.NULL, obj.getType("anullp"));
        Assert.assertEquals(JsonType.OBJECT, obj.getType("aobjo"));
        Assert.assertEquals(ableo.generateJson(), obj.getJsonObject("aobjo"));
        Assert.assertEquals(JsonType.NULL, obj.getType("anullj"));
        
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

}
