package com.bantouyan.json.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.bantouyan.json.*;
import com.bantouyan.json.Json.JsonType;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Ignore;

public class TestJsonArray
{
    @Test
    public void jsonArray_new_count_isEmpty_getType_equals_iterator_getString_clone_remove()
    {
        JsonArray ary = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonArray sary = new JsonArray();
        sary.append(obj);
        
        Assert.assertEquals(0, ary.count());
        Assert.assertTrue(ary.isEmpty());
        
        ary.append();
        ary.append(true);
        ary.append(99);
        ary.append(5.5);
        ary.append("str");
        ary.append(sary);
        ary.append(obj);
        
        Assert.assertEquals(7, ary.count());
        Assert.assertFalse(ary.isEmpty());
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(1));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(2));
        Assert.assertEquals(JsonType.FLOAT, ary.getType(3));
        Assert.assertEquals(JsonType.STRING, ary.getType(4));
        Assert.assertEquals(JsonType.ARRAY, ary.getType(5));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(6));
        
        ArrayList<Json> list = new ArrayList<Json>();
        for(int i=0; i<ary.count(); i++)
        {
            Json json = ary.get(i);
            list.add(json);
            Assert.assertEquals(json.getType(), ary.getType(i));
        }
        JsonArray ary2 = new JsonArray(list);
        
        Assert.assertEquals(ary, ary2);
        Assert.assertEquals(ary.count(), ary2.count());
        Assert.assertFalse(ary2.isEmpty());
        
        Assert.assertEquals(JsonType.ARRAY, ary.getType());
        Assert.assertEquals(JsonType.ARRAY, ary2.getType());
        
        for(Json js: ary)
        {
            Assert.assertTrue(js.count() >= 0);
        }

        ary.append();
        ary.append(true);
        ary.append(99);
        ary.append(5.5);
        ary.append("str");
        ary.append(sary);
        ary.append(obj);
        
        Assert.assertEquals("null", ary.getString(0));
        Assert.assertEquals("true", ary.getString(1));
        Assert.assertEquals("99", ary.getString(2));
        Assert.assertEquals("5.5", ary.getString(3));
        Assert.assertEquals("str", ary.getString(4));
        Assert.assertEquals("[{}]", ary.getString(5));
        Assert.assertEquals("{}", ary.getString(6));
        
        JsonArray ary3 = ary2.clone();
        Assert.assertEquals(ary2, ary3);
        Assert.assertFalse(ary2 == ary3);
        for(int i=0; i<ary2.count(); i++)
        {
            Json j2 = ary2.get(i);
            Json j3 = ary3.get(i);
            Assert.assertEquals(j2, j3);
            if(j2 instanceof JsonPrimitive)
                Assert.assertTrue(j2 == j3);
            else
                Assert.assertFalse(j2 == j3);
        }
        
        JsonArray ary4 = new JsonArray();
        ary4.append(3);
        ary4.append(4);
        ary4.append(5);
        Assert.assertEquals("[3,4,5]", ary4.generateJsonText());
        ary4.remove(1);
        Assert.assertEquals("[3,5]", ary4.generateJsonText());
    }
    
    @Test
    public void jsonArray_newCollection()
    {
        //invalid value
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(new Date());
        try
        {
            Json json = new JsonArray(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }

        try
        {
            Json json = new JsonArray(list, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }

        //circle
        list = new ArrayList<Object>();
        list.add(list);
        try
        {
            Json json = new JsonArray(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Collection."));
        }

        try
        {
            Json json = new JsonArray(list, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Collection."));
        }
        
        //normal

        HashMap<Object, Object> map = new HashMap<Object, Object>();
        list = new ArrayList<Object>();
        list.add(null);
        list.add(false);
        list.add("\"String\"");
        list.add(356);
        list.add(4.2e-1);
        list.add(map);
        map.put("NULL", null);
        
        Json json = new JsonArray(list);
        System.out.println("===================================");
        String jsonStr = json.generateJsonText();
        System.out.println(jsonStr);
        Assert.assertEquals("[null,false,\"\\\"String\\\"\",356,0.42,{\"NULL\":null}]", jsonStr);
        
        //JsonParser
        list = new ArrayList<Object>();
        list.add("v1");
        list.add("&&v2");
        map = new HashMap<Object, Object>();
        map.put("**name", "v1");
        map.put("num", "&&30");
        list.add(map);
        
        JsonArray jary = new JsonArray(list, parser);
        System.out.println(jary);
        Assert.assertEquals("[\"v1\",\"v2\",{\"num\":\"30\",\"name\":\"v1\"}]", jary.generateJsonText());
    }
    
    @Test
    public void booleanElement_get_canTo()
    {
        System.out.println("===============================");
        JsonObject obj = new JsonObject();
        JsonArray ary = new JsonArray();
        JsonArray ary2 = new JsonArray();
        ary.append();
        ary.append(2);
        ary.append(3.5);
        ary.append(obj);
        ary.append(ary2);
        ary.append("Not");
        ary.append("TRue");
        ary.append(" true ");
        ary.append(true);
        
        String errStr = "";
        for(int i=0; i<ary.count(); i++)
        {
            Boolean val = false;
            try
            {
                val = ary.getBoolean(i);
            } 
            catch (JsonException e)
            {
                String msg = e.getMessage();
                System.out.println(msg);
                Assert.assertEquals("Cannot transfer element at " + i + " to boolean value.", msg);
                errStr += i;
            }
            if(i>5)
            {
                Assert.assertTrue(val);
                Assert.assertTrue(ary.canToBoolean(i));
            }
            else
            {
                Assert.assertFalse(ary.canToBoolean(i));
            }
        }
        Assert.assertEquals("012345", errStr);
    }
    
    @Test
    public void longElement_get_canTo()
    {
        System.out.println("===============================");
        JsonObject obj = new JsonObject();
        JsonArray ary = new JsonArray();
        JsonArray ary2 = new JsonArray();
        ary.append();
        ary.append(3.5);
        ary.append(obj);
        ary.append(ary2);
        ary.append(true); //4
        ary.append("Not");
        ary.append("-66.66");
        ary.append(" -66L"); //7
        ary.append("66 L"); //8
        ary.append(-66);
        ary.append(" -66 "); //10
        ary.append(66);
        ary.append(" 66 ");
        ary.append(" +66 ");
        

        String errStr = "";
        for(int i=0; i<ary.count(); i++)
        {
            long val = 0;
            try
            {
                val = ary.getLong(i);
            } 
            catch (JsonException e)
            {
                String msg = e.getMessage();
                System.out.println(msg);
                String cmpMsg = "Cannot transfer element at " + i + " to long value.";
                Assert.assertEquals(cmpMsg, msg);
                errStr += i;
            }
            if(i>8)
            {
                Assert.assertTrue(ary.canToLong(i));
                if(i>10)
                {
                    Assert.assertEquals(66, val);
                }
                else
                {
                    Assert.assertEquals(-66, val);
                }
                
            }
            else
            {
                Assert.assertFalse(ary.canToLong(i));
            }
        }
        Assert.assertEquals("012345678", errStr);
    }
    
    @Test
    public void doubleElement_get_canTo()
    {
        System.out.println("===============================");
        JsonObject obj = new JsonObject();
        JsonArray ary = new JsonArray();
        JsonArray ary2 = new JsonArray();
        ary.append();
        ary.append(obj);
        ary.append(ary2);
        ary.append(true);
        ary.append("Not"); //4
        ary.append(" -66L ");
        ary.append(" 66 D");
        ary.append(" 0.0078125F");//7
        ary.append(" -0.0078125F");
        ary.append(" +0.0078125F");
        ary.append(" 7.8125e-2F"); //10
        ary.append(" +7.8125 e-2");
        ary.append(" -7.8125e -2"); //12
        ary.append(" 66 ");
        ary.append(" +66");
        ary.append(66); //15
        ary.append(" -66 ");
        ary.append(-66); //17
        ary.append(0.0078125);
        ary.append(" 0.0078125 ");
        ary.append(" +0.0078125 ");
        ary.append(7.8125e-3);
        ary.append("7.8125e-3");
        ary.append("+7.8125e-3");
        ary.append("+0.000078125e+2"); //24
        ary.append(-7.8125e-3);
        ary.append("-7.8125e-3");
        ary.append(-0.0078125);
        ary.append(" -0.0078125 "); 
        ary.append("-0.000078125e+2");
        
        

        String errStr = "";
        for(int i=0; i<ary.count(); i++)
        {
            double val = 0.0;
            try
            {
                val = ary.getDouble(i);
            } 
            catch (JsonException e)
            {
                String msg = e.getMessage();
                System.out.println(msg);
                String cmpMsg = "Cannot transfer element at " + i + " to double value.";
                Assert.assertEquals(cmpMsg, msg);
                errStr += i;
            }
            
            if(i>12)
            {
                System.out.println(i + " " + val);
                Assert.assertTrue(ary.canToDouble(i));
                if(i>24)
                    Assert.assertEquals(-0.0078125, val, 0.1e-10);
                else if(i>17)
                    Assert.assertEquals(0.0078125, val, 0.1e-10);
                else if(i>15)
                    Assert.assertEquals(-66.0, val, 0.1e-10);
                else 
                    Assert.assertEquals(66.0, val, 0.1e-10);
            }
            else
            {
                Assert.assertFalse(ary.canToDouble(i));
            }
        }

        Assert.assertEquals("0123456789101112", errStr);
    }

    @Test
    public void arrayElement_get_canTo()
    {
        System.out.println("===============================");
        JsonObject obj = new JsonObject();
        JsonArray ary = new JsonArray();
        JsonArray ary2 = new JsonArray();
        ary.append();
        ary.append(true);
        ary.append(66);
        ary.append(3.5);
        ary.append("String");
        ary.append(obj); //5
        ary.append(ary2);
        

        String errStr = "";
        for(int i=0; i<ary.count(); i++)
        {
            Json val = null;
            try
            {
                val = ary.getJsonArray(i);
            } 
            catch (JsonException e)
            {
                String msg = e.getMessage();
                System.out.println(msg);
                String cmpMsg = "Cannot transfer element at " + i + " to JsonArray value.";
                Assert.assertEquals(cmpMsg, msg);
                errStr += i;
            }
            if(i==6)
            {
                Assert.assertTrue(ary.canToJsonArray(i));
                Assert.assertTrue(val instanceof JsonArray);
            }
            else
            {
                Assert.assertFalse(ary.canToJsonArray(i));
            }
        }
        Assert.assertEquals("012345", errStr);
    }

    @Test
    public void objectElement_get_canTo()
    {
        System.out.println("===============================");
        JsonObject obj = new JsonObject();
        JsonArray ary = new JsonArray();
        JsonArray ary2 = new JsonArray();
        ary.append();
        ary.append(true);
        ary.append(66);
        ary.append(3.5);
        ary.append("String");
        ary.append(ary2); //5
        ary.append(obj);
        

        String errStr = "";
        for(int i=0; i<ary.count(); i++)
        {
            Json val = null;
            try
            {
                val = ary.getJsonObject(i);
            } 
            catch (JsonException e)
            {
                String msg = e.getMessage();
                System.out.println(msg);
                String cmpMsg = "Cannot transfer element at " + i + " to JsonObject value.";
                Assert.assertEquals(cmpMsg, msg);
                errStr += i;
            }
            if(i==6)
            {
                Assert.assertTrue(ary.canToJsonObject(i));
                Assert.assertTrue(val instanceof JsonObject);
            }
            else
            {
                Assert.assertFalse(ary.canToJsonObject(i));
            }
        }
        Assert.assertEquals("012345", errStr);
    }
    
    @Test
    public void append_set_insert()
    {        
        Jsonable jableObj = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        Jsonable jableNull = new Jsonable(){public Json generateJson()
        {JsonArray jary = new JsonArray(); jary.append(); return jary.get(0);}};
        Jsonable nullJable = null;
        JsonArray ary = new JsonArray();
        JsonArray subAry = new JsonArray();
        JsonArray nullAry = null;
        JsonObject obj = new JsonObject();
        JsonObject nullObj = null;
        String str = "String";
        String nullStr = null;
        
        ary.append();      //0
        ary.append(true);  //1
        ary.append(30);    //2
        ary.append(33.3);  //3
        ary.append(str);   //4
        ary.append(nullStr);  //5
        ary.append(subAry);  //6
        ary.append(nullAry);  //7
        ary.append(obj);   //8
        ary.append(nullObj);  //9
        ary.append(jableObj); //10
        ary.append(jableNull); //11
        ary.append(nullJable); //12
        
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(1));
        Assert.assertEquals(true, ary.getBoolean(1));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(2));
        Assert.assertEquals(30, ary.getLong(2));
        Assert.assertEquals(JsonType.FLOAT, ary.getType(3));
        Assert.assertEquals(33.3, ary.getDouble(3), 0.1e-10);
        Assert.assertEquals(JsonType.STRING, ary.getType(4));
        Assert.assertEquals(str, ary.getString(4));
        Assert.assertEquals(JsonType.NULL, ary.getType(5));
        Assert.assertEquals(JsonType.ARRAY, ary.getType(6));
        Assert.assertEquals(subAry, ary.getJsonArray(6));
        Assert.assertEquals(JsonType.NULL, ary.getType(7));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(8));
        Assert.assertEquals(obj, ary.getJsonObject(8));
        Assert.assertEquals(JsonType.NULL, ary.getType(9));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(10));
        Assert.assertEquals(jableObj.generateJson(), ary.getJsonObject(10));
        Assert.assertEquals(JsonType.NULL, ary.getType(11));
        Assert.assertEquals(JsonType.NULL, ary.getType(12));
        
        ary.set(0, str);
        ary.set(1, nullAry);
        ary.set(2, nullObj);
        ary.set(3, nullJable);
        ary.set(4, nullStr);
        ary.set(5, subAry);
        ary.set(6);
        ary.set(7, obj);
        ary.set(8, false);
        ary.set(9, jableObj);
        ary.set(10, jableNull);
        ary.set(11, 22);
        ary.set(12, 23.3);

        Assert.assertEquals(JsonType.STRING, ary.getType(0));
        Assert.assertEquals(str, ary.getString(0));
        Assert.assertEquals(JsonType.NULL, ary.getType(1));
        Assert.assertEquals("null", ary.getString(1));
        Assert.assertEquals(JsonType.NULL, ary.getType(2));
        Assert.assertEquals(JsonType.NULL, ary.getType(3));
        Assert.assertEquals(JsonType.NULL, ary.getType(4));
        Assert.assertEquals(JsonType.ARRAY, ary.getType(5));
        Assert.assertEquals(subAry, ary.getJsonArray(5));
        Assert.assertEquals(JsonType.NULL, ary.getType(6));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(7));
        Assert.assertEquals(obj, ary.getJsonObject(7));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(8));
        Assert.assertEquals(false, ary.getBoolean(8));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(9));
        Assert.assertEquals(jableObj.generateJson(), ary.getJsonObject(9));
        Assert.assertEquals(JsonType.NULL, ary.getType(10));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(11));
        Assert.assertEquals(22, ary.getLong(11));
        Assert.assertEquals(JsonType.FLOAT, ary.getType(12));
        Assert.assertEquals(23.3, ary.getDouble(12), 0.1e-10);
        
        ary.insert(0, nullJable); //12
        ary.insert(0, jableNull); //11
        ary.insert(0, jableObj); //10
        ary.insert(0, nullObj);  //9
        ary.insert(0, obj);   //8
        ary.insert(0, nullAry);  //7
        ary.insert(0, subAry);  //6
        ary.insert(0, nullStr);  //5
        ary.insert(0, str);   //4
        ary.insert(0, 33.3);  //3
        ary.insert(0, 30);    //2
        ary.insert(0, true);  //1
        ary.insert(0);      //0
        
        Assert.assertEquals(JsonType.NULL, ary.getType(0));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(1));
        Assert.assertEquals(true, ary.getBoolean(1));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(2));
        Assert.assertEquals(30, ary.getLong(2));
        Assert.assertEquals(JsonType.FLOAT, ary.getType(3));
        Assert.assertEquals(33.3, ary.getDouble(3), 01.e-10);
        Assert.assertEquals(JsonType.STRING, ary.getType(4));
        Assert.assertEquals(str, ary.getString(4));
        Assert.assertEquals(JsonType.NULL, ary.getType(5));
        Assert.assertEquals(JsonType.ARRAY, ary.getType(6));
        Assert.assertEquals(subAry, ary.getJsonArray(6));
        Assert.assertEquals(JsonType.NULL, ary.getType(7));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(8));
        Assert.assertEquals(obj, ary.getJsonObject(8));
        Assert.assertEquals(JsonType.NULL, ary.getType(9));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(10));
        Assert.assertEquals(jableObj.generateJson(), ary.getJsonObject(10));
        Assert.assertEquals(JsonType.NULL, ary.getType(11));
        Assert.assertEquals(JsonType.NULL, ary.getType(12));

        Assert.assertEquals(JsonType.STRING, ary.getType(13));
        Assert.assertEquals(str, ary.getString(13));
        Assert.assertEquals(JsonType.NULL, ary.getType(14));
        Assert.assertEquals(JsonType.NULL, ary.getType(15));
        Assert.assertEquals(JsonType.NULL, ary.getType(16));
        Assert.assertEquals(JsonType.NULL, ary.getType(17));
        Assert.assertEquals(JsonType.ARRAY, ary.getType(18));
        Assert.assertEquals(subAry, ary.getJsonArray(18));
        Assert.assertEquals(JsonType.NULL, ary.getType(19));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(20));
        Assert.assertEquals(obj, ary.getJsonObject(20));
        Assert.assertEquals(JsonType.BOOLEAN, ary.getType(21));
        Assert.assertEquals(false, ary.getBoolean(21));
        Assert.assertEquals(JsonType.OBJECT, ary.getType(22));
        Assert.assertEquals(jableObj.generateJson(), ary.getJsonObject(22));
        Assert.assertEquals(JsonType.NULL, ary.getType(23));
        Assert.assertEquals(JsonType.INTEGER, ary.getType(24));
        Assert.assertEquals(22, ary.getLong(24));
        Assert.assertEquals(JsonType.FLOAT, ary.getType(25));
        Assert.assertEquals(23.3, ary.getDouble(25), 0.1e-10);
    }
    
    @Test
    public void appendAll_insertAll_exception()
    {
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        
        //invalid value
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(new Date());
        try
        {
            jary.appendAll(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        try
        {
            jary.appendAll(list, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        try
        {
            jary.insertAll(0, list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }
        
        try
        {
            jary.insertAll(0, list, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.indexOf("Cannot parse value: ") == 0);
        }

        //circle
        list = new ArrayList<Object>();
        list.add(list);
        try
        {
            jary.appendAll(list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Collection."));
        }

        try
        {
            jary.appendAll(list, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Collection."));
        }

        try
        {
            jary.insertAll(0, list);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Collection."));
        }

        try
        {
            jary.insertAll(0, list, parser);
        }
        catch(JsonException e)
        {
            String msg = e.getMessage();
            System.out.println(msg + "\n-----------------------------------");
            Assert.assertTrue(msg.equals("Circle reference exists in this Collection."));
        }
    }
    
    @Test
    public void appendAll_insertAll_normal()
    {
        Jsonable jableObj = new Jsonable(){public Json generateJson(){return new JsonObject();}};
        Jsonable jableNull = new Jsonable(){public Json generateJson(){return new JsonPrimitive();}};
        JsonArray subAry = new JsonArray();
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(33);
        jary.append(33.33);
        jary.append("Str");
        jary.append(new JsonArray());
        jary.append(new JsonObject());
        ArrayList<Object> list = new ArrayList();
        list.add(null);
        list.add(true);
        list.add(33);
        list.add(33.33);
        list.add("Str");
        list.add(new JsonArray());
        list.add(new JsonObject());
        list.add(jableNull);
        list.add(jableObj);
        JsonArray jary2 = jary.clone();
        jary2.append(jableNull);
        jary2.append(jableObj);
        
        //append JsonArray
        JsonArray ary = new JsonArray();
        ary.appendAll(jary);
        Assert.assertEquals(jary, ary);
        
        //insert JsonArray
        ary.clear();
        ary.append();
        ary.insertAll(0, jary);
        int pos = jary.count();
        Assert.assertEquals(JsonType.NULL, ary.getType(pos));
        ary.remove(pos);
        Assert.assertEquals(jary, ary);
        
        //append Collection
        ary.clear();
        ary.appendAll(list);
        Assert.assertEquals(jary2, ary);
        
        //insert Collection
        ary.clear();
        ary.append();
        ary.insertAll(0, list);
        pos = list.size();
        Assert.assertEquals(JsonType.NULL, ary.getType(pos));
        ary.remove(pos);
        Assert.assertEquals(jary2, ary);
        
        // for parser
        list = new ArrayList<Object>();
        list.add("v1");
        list.add("&&v2");
        HashMap map = new HashMap<Object, Object>();
        map.put("**name", "v1");
        map.put("num", "&&30");
        list.add(map);

        //append parser
        ary.clear();
        ary.appendAll(list, parser);
        System.out.println(ary);
        Assert.assertEquals("[\"v1\",\"v2\",{\"num\":\"30\",\"name\":\"v1\"}]", ary.generateJsonText());
        
        //insert parser
        ary.clear();
        ary.append();
        ary.insertAll(0, list, parser);
        System.out.println(ary);
        Assert.assertEquals("[\"v1\",\"v2\",{\"num\":\"30\",\"name\":\"v1\"},null]", ary.generateJsonText());
    }
        
    @Test
    public void double_NaN_Infinity()
    {
        double nan = Double.NaN;
        double posInfinity = Double.POSITIVE_INFINITY;
        double negInfinity = Double.NEGATIVE_INFINITY;
        
        JsonArray ary = new JsonArray();
        ary.append(nan);
        ary.append(posInfinity);
        ary.append(negInfinity);        
        Assert.assertEquals(JsonType.STRING, ary.getType(0));
        Assert.assertEquals(JsonType.STRING, ary.getType(1));
        Assert.assertEquals(JsonType.STRING, ary.getType(2));
        Assert.assertEquals("NaN", ary.getString(0));
        Assert.assertEquals("Infinity", ary.getString(1));
        Assert.assertEquals("-Infinity", ary.getString(2));
        
        ary.insert(0, negInfinity);
        ary.insert(0, posInfinity);
        ary.insert(0, nan);      
        Assert.assertEquals(JsonType.STRING, ary.getType(0));
        Assert.assertEquals(JsonType.STRING, ary.getType(1));
        Assert.assertEquals(JsonType.STRING, ary.getType(2));
        Assert.assertEquals("NaN", ary.getString(0));
        Assert.assertEquals("Infinity", ary.getString(1));
        Assert.assertEquals("-Infinity", ary.getString(2));
        
        ary.set(0, posInfinity);
        ary.set(1, negInfinity);
        ary.set(2, nan);      
        Assert.assertEquals(JsonType.STRING, ary.getType(0));
        Assert.assertEquals(JsonType.STRING, ary.getType(1));
        Assert.assertEquals(JsonType.STRING, ary.getType(2));
        Assert.assertEquals("Infinity", ary.getString(0));
        Assert.assertEquals("-Infinity", ary.getString(1));
        Assert.assertEquals("NaN", ary.getString(2));
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
