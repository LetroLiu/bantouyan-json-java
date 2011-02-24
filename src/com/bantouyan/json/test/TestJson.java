package com.bantouyan.json.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

import com.bantouyan.json.Json;
import com.bantouyan.json.JsonArray;
import com.bantouyan.json.JsonException;
import com.bantouyan.json.JsonObject;
import com.bantouyan.json.JsonParseException;

public class TestJson
{
    @Test
    public void existsCircle()
    {
        JsonObject obj = new JsonObject();
        JsonArray ary = new JsonArray();
        obj.add("NULL");
        ary.append();
        obj.set("number", 30);
        ary.append(30);
        
        obj.set("array", ary);
        ary.set(0, obj);
        Assert.assertTrue(obj.existsCircle());
        Assert.assertTrue(ary.existsCircle());
        
        obj.remove("array");
        Assert.assertFalse(obj.existsCircle());
        Assert.assertFalse(ary.existsCircle());
        
        obj.set("array", ary);
        ary.set(0);
        Assert.assertFalse(obj.existsCircle());
        Assert.assertFalse(ary.existsCircle());
        
        obj.remove("array");
        Assert.assertFalse(obj.existsCircle());
        Assert.assertFalse(ary.existsCircle());
    }
    
    @Test
    public void jsonEqualsA() throws IOException, JsonParseException
    {
        String str = "[-1, 10.3, 33e4, true, false, null,"
                + "{INT: -1, FLOAT: 22.4, BOOL: true, \"NULL\": null,"
                + "\"array\":[],"
                + "\"object\":{}"
                + "},[null, \"Error\", {}, []]"
                + "]";
        Json jsona = Json.parseJsonText(str);

        JsonArray aryNull = new JsonArray();
        JsonObject objNull = new JsonObject();
        JsonArray jsonb = new JsonArray();
        jsonb.append(-1);
        jsonb.append(1.03e1);
        jsonb.append(330000.0);
        jsonb.append(true);
        jsonb.append(false);
        jsonb.append();
        JsonObject jobjs = new JsonObject();
        jobjs.set("INT", -1);
        jobjs.set("FLOAT", 22.4);
        jobjs.set("BOOL", true);
        jobjs.set("NULL");
        jobjs.set("array", aryNull);
        jobjs.set("object", objNull);
        jsonb.append(jobjs);
        JsonArray jarys = new JsonArray();
        jarys.append();
        jarys.append("Error");
        jarys.append(objNull);
        jarys.append(aryNull);
        jsonb.append(jarys);
        
        Assert.assertEquals(jsona, jsonb);
        
        String sa = jsona.toString();
        String sb = jsonb.toString();
        Assert.assertEquals(sa, sb);
        
        String sc = jsona.generateJsonText(true);
        String sd = jsonb.generateJsonText(true);
        Assert.assertEquals(sc, sd);
        
        String se = jsona.generateJsonText(false);
        String sf = jsonb.generateJsonText(false);
        Assert.assertEquals(se, sf);
    }
    
    @Test
    public void jsonEqualsB()
    {
        JsonObject objA = new JsonObject();
        JsonObject objB = new JsonObject();
        JsonArray aryA = new JsonArray();
        JsonArray aryB = new JsonArray();
        
        objA.set("NULL");
        objB.set("NULL");
        objA.set("Number", 33);
        objB.set("Number", 33);
        Assert.assertEquals(objA, objB);
        
        aryA.append(1);
        aryB.append(1);
        aryA.append(true);
        aryB.append(true);
        aryA.append(objA);
        aryA.append(objA);
        aryB.append(objB);
        aryB.append(objA);
        Assert.assertEquals(aryA, aryB);
        
        JsonArray aryC = new JsonArray();
        JsonArray aryD = new JsonArray();
        objA.add("ary1", aryC);
        objA.add("ary2", aryD);
        objB.add("ary1", aryD);
        objB.add("ary2", aryD);
        Assert.assertEquals(aryA, aryB);
    }

    @Test(expected = JsonException.class)
    public void genText_circleA()
    {
        JsonArray jary = new JsonArray();
        jary.append(jary);
        
        try
        {
            String text = jary.generateJsonText(false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void genText_circleB()
    {
        JsonObject jobj = new JsonObject();
        jobj.add("obj", jobj);
        
        try
        {
            String text = jobj.generateJsonText(false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void genText_circleC()
    {
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jobj.add("ary", jary);
        jary.append(jobj);
        
        try
        {
            String text = jobj.generateJsonText(false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void genText_circleD()
    {
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jary.append(jobj);
        jobj.add("ary", jary);
        
        try
        {
            String text = jary.generateJsonText(false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }

    @Test
    public void genText_simpleObj()
    {
        JsonObject jobj = new JsonObject();
        jobj.add("Null");
        jobj.add("True", true);
        jobj.add("False", false);
        jobj.add("Integer", 100);
        jobj.add("Float", 30.3);
        jobj.add("String", "str\\nstr\\tstr\\u0048str");
        
        String str1 = jobj.generateJsonText(true);
        String str2 = jobj.generateJsonText();
        Assert.assertEquals(str1, str2);
        
        String str3 = jobj.toString();
        Assert.assertEquals(str2, str3);

        str1 = str1.replace("\"Null\"", "Null");
        str1 = str1.replace("\"True\"", "True");
        str1 = str1.replace("\"False\"", "False");
        str1 = str1.replace("\"Integer\"", "Integer");
        str1 = str1.replace("\"Float\"", "Float");
        str1 = str1.replace("\"String\"", "String");
        String str4 = jobj.generateJsonText(false);
        Assert.assertEquals(str1, str4);
    }
    
    @Test
    public void genText_simpleAry()
    {
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(false);
        jary.append(10);
        jary.append(20.3);
        jary.append(2.03e1);
        jary.append("null");
        
        String str1 = jary.generateJsonText(false);
        String str2 = jary.generateJsonText(true);
        String str3 = jary.generateJsonText();
        String str4 = jary.toString();
        
        Assert.assertEquals("[null,true,false,10,20.3,20.3,\"null\"]", str1);
        Assert.assertEquals(str1, str2);
        Assert.assertEquals(str1, str3);
        Assert.assertEquals(str1, str4);
    }
    
    @Test
    public void genText_complexObj()
    {
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(false);
        jary.append(10);
        jary.append(20.3);
        jary.append(2.03e1);
        jary.append("null");
        jary.append(new JsonArray());
        jary.append(new JsonObject());
        
        JsonObject jobj = new JsonObject();
        jobj.add("Null");
        jobj.add("True", true);
        jobj.add("False", false);
        jobj.add("Integer", 100);
        jobj.add("Float", 30.3);
        jobj.add("String", "str\\nstr\\tstr\\u0020str");
        jobj.add("Object", new JsonObject());
        jobj.add("Array", new JsonArray());
        jobj.add("Pointer", jary);
        
        String str1 = jobj.generateJsonText(true);
        String str2 = jobj.generateJsonText();
        Assert.assertEquals(str1, str2);
        
        String str3 = jobj.toString();
        Assert.assertEquals(str2, str3);
        
        str1 = str1.replace("\"Null\"", "Null");
        str1 = str1.replace("\"True\"", "True");
        str1 = str1.replace("\"False\"", "False");
        str1 = str1.replace("\"Integer\"", "Integer");
        str1 = str1.replace("\"Float\"", "Float");
        str1 = str1.replace("\"String\"", "String");
        str1 = str1.replace("\"Object\"", "Object");
        str1 = str1.replace("\"Array\"", "Array");
        str1 = str1.replace("\"Pointer\"", "Pointer");
        String str4 = jobj.generateJsonText(false);
        Assert.assertEquals(str1, str4);
    }
    
    @Test
    public void genText_complexAry()
    {
        JsonObject jobj = new JsonObject();
        jobj.add("Null");
        jobj.add("True", true);
        jobj.add("False", false);
        jobj.add("Integer", 100);
        jobj.add("Float", 30.3);
        jobj.add("String", "str\\nstr\\tstr\\u0020str");
        jobj.add("Object", new JsonObject());
        jobj.add("Array", new JsonArray());
        
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(false);
        jary.append(10);
        jary.append(20.3);
        jary.append(2.03e1);
        jary.append("null");
        jary.append(new JsonArray());
        jary.append(new JsonObject());
        jary.insert(0, jobj);
        
        String str1 = jary.generateJsonText(true);
        String str2 = jary.generateJsonText();
        Assert.assertEquals(str1, str2);
        
        String str3 = jary.toString();
        Assert.assertEquals(str2, str3);
        
        str1 = str1.replace("\"Null\"", "Null");
        str1 = str1.replace("\"True\"", "True");
        str1 = str1.replace("\"False\"", "False");
        str1 = str1.replace("\"Integer\"", "Integer");
        str1 = str1.replace("\"Float\"", "Float");
        str1 = str1.replace("\"String\"", "String");
        str1 = str1.replace("\"Object\"", "Object");
        str1 = str1.replace("\"Array\"", "Array");
        str1 = str1.replace("\"Pointer\"", "Pointer");
        String str4 = jary.generateJsonText(false);
        Assert.assertEquals(str1, str4);
    }

    @Test(expected = JsonException.class)
    public void outWriter_circleA() throws IOException
    {
        JsonArray jary = new JsonArray();
        jary.append(jary);
        
        try
        {
            StringWriter writer = new StringWriter();
            jary.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void outWriter_circleB() throws IOException
    {
        JsonObject jobj = new JsonObject();
        jobj.add("obj", jobj);
        
        try
        {
            StringWriter writer = new StringWriter();
            jobj.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void outWriter_circleC() throws IOException
    {
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jobj.add("ary", jary);
        jary.append(jobj);
        
        try
        {
            StringWriter writer = new StringWriter();
            jobj.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void outWriter_circleD() throws IOException
    {
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jary.append(jobj);
        jobj.add("ary", jary);
        
        try
        {
            StringWriter writer = new StringWriter();
            jary.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }

    @Test(expected = JsonException.class)
    public void outWriter_circlePA() throws IOException
    {
        JsonArray jary = new JsonArray();
        jary.append(jary);
        
        try
        {
            PrintWriter writer = new PrintWriter(new StringWriter());
            jary.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void outWriter_circlePB() throws IOException
    {
        JsonObject jobj = new JsonObject();
        jobj.add("obj", jobj);
        
        try
        {
            PrintWriter writer = new PrintWriter(new StringWriter());
            jobj.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void outWriter_circlePC() throws IOException
    {
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jobj.add("ary", jary);
        jary.append(jobj);
        
        try
        {
            PrintWriter writer = new PrintWriter(new StringWriter());
            jobj.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test(expected = JsonException.class)
    public void outWriter_circlePD() throws IOException
    {
        JsonObject jobj = new JsonObject();
        JsonArray jary = new JsonArray();
        jary.append(jobj);
        jobj.add("ary", jary);
        
        try
        {
            PrintWriter writer = new PrintWriter(new StringWriter());
            jary.outputToWriter(writer, false);
        } 
        catch (JsonException e)
        {
            String msg = e.getMessage();
            Assert.assertEquals("Circle reference exists in this Json.", msg);
            throw e;
        }
    }
    
    @Test
    public void outWriter_simpleObj() throws IOException, JsonException
    {
        JsonObject jobj = new JsonObject();
        jobj.add("Null");
        jobj.add("True", true);
        jobj.add("False", false);
        jobj.add("Integer", 100);
        jobj.add("Float", 30.3);
        jobj.add("String", "str\\nstr\\tstr\\u0048str");
        
        String str1 = jobj.generateJsonText(true);
        String str2 = jobj.generateJsonText(false);
        
        StringWriter writer3 = new StringWriter();
        jobj.outputToWriter(writer3, true);
        String str3 = writer3.toString();
        StringWriter writer4 = new StringWriter();
        jobj.outputToWriter(writer4, false);
        String str4 = writer4.toString();
        
        StringWriter writer5 = new StringWriter();
        PrintWriter pw5 = new PrintWriter(writer5);
        jobj.outputToWriter(pw5, true);
        String str5 = writer5.toString();
        StringWriter writer6 = new StringWriter();
        PrintWriter pw6 = new PrintWriter(writer6);
        jobj.outputToWriter(pw6, false);
        String str6 = writer6.toString();
        
        Assert.assertEquals(str1, str3);
        Assert.assertEquals(str1, str5);
        Assert.assertEquals(str2, str4);
        Assert.assertEquals(str2, str6);
    }
    
    @Test
    public void outWriter_simpleAry() throws IOException, JsonException
    {
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(false);
        jary.append(10);
        jary.append(20.3);
        jary.append(2.03e1);
        jary.append("null");
        
        String str1 = jary.generateJsonText(true);
        String str2 = jary.generateJsonText(false);
        
        StringWriter writer3 = new StringWriter();
        jary.outputToWriter(writer3, true);
        String str3 = writer3.toString();
        StringWriter writer4 = new StringWriter();
        jary.outputToWriter(writer4, false);
        String str4 = writer4.toString();
        
        StringWriter writer5 = new StringWriter();
        PrintWriter pw5 = new PrintWriter(writer5);
        jary.outputToWriter(pw5, true);
        String str5 = writer5.toString();
        StringWriter writer6 = new StringWriter();
        PrintWriter pw6 = new PrintWriter(writer6);
        jary.outputToWriter(pw6, false);
        String str6 = writer6.toString();
        
        Assert.assertEquals(str1, str3);
        Assert.assertEquals(str1, str5);
        Assert.assertEquals(str2, str4);
        Assert.assertEquals(str2, str6);
        Assert.assertEquals(str1, str2);
    }
    
    @Test
    public void outWriter_complexObj() throws IOException, JsonException
    {
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(false);
        jary.append(10);
        jary.append(20.3);
        jary.append(2.03e1);
        jary.append("null");
        jary.append(new JsonArray());
        jary.append(new JsonObject());
        
        JsonObject jobj = new JsonObject();
        jobj.add("Null");
        jobj.add("True", true);
        jobj.add("False", false);
        jobj.add("Integer", 100);
        jobj.add("Float", 30.3);
        jobj.add("String", "str\\nstr\\tstr\\u0020str");
        jobj.add("Object", new JsonObject());
        jobj.add("Array", new JsonArray());
        jobj.add("Pointer", jary);
        
        String str1 = jobj.generateJsonText(true);
        String str2 = jobj.generateJsonText(false);
        
        StringWriter writer3 = new StringWriter();
        jobj.outputToWriter(writer3, true);
        String str3 = writer3.toString();
        StringWriter writer4 = new StringWriter();
        jobj.outputToWriter(writer4, false);
        String str4 = writer4.toString();
        
        StringWriter writer5 = new StringWriter();
        PrintWriter pw5 = new PrintWriter(writer5);
        jobj.outputToWriter(pw5, true);
        String str5 = writer5.toString();
        StringWriter writer6 = new StringWriter();
        PrintWriter pw6 = new PrintWriter(writer6);
        jobj.outputToWriter(pw6, false);
        String str6 = writer6.toString();
        
        Assert.assertEquals(str1, str3);
        Assert.assertEquals(str1, str5);
        Assert.assertEquals(str2, str4);
        Assert.assertEquals(str2, str6);
    }
    
    @Test
    public void outWriter_complexAry() throws IOException, JsonException
    {
        JsonObject jobj = new JsonObject();
        jobj.add("Null");
        jobj.add("True", true);
        jobj.add("False", false);
        jobj.add("Integer", 100);
        jobj.add("Float", 30.3);
        jobj.add("String", "str\\nstr\\tstr\\u0020str");
        jobj.add("Object", new JsonObject());
        jobj.add("Array", new JsonArray());
        
        JsonArray jary = new JsonArray();
        jary.append();
        jary.append(true);
        jary.append(false);
        jary.append(10);
        jary.append(20.3);
        jary.append(2.03e1);
        jary.append("null");
        jary.append(new JsonArray());
        jary.append(new JsonObject());
        jary.insert(0, jobj);
        
        String str1 = jary.generateJsonText(true);
        String str2 = jary.generateJsonText(false);
        
        StringWriter writer3 = new StringWriter();
        jary.outputToWriter(writer3, true);
        String str3 = writer3.toString();
        StringWriter writer4 = new StringWriter();
        jary.outputToWriter(writer4, false);
        String str4 = writer4.toString();
        
        StringWriter writer5 = new StringWriter();
        PrintWriter pw5 = new PrintWriter(writer5);
        jary.outputToWriter(pw5, true);
        String str5 = writer5.toString();
        StringWriter writer6 = new StringWriter();
        PrintWriter pw6 = new PrintWriter(writer6);
        jary.outputToWriter(pw6, false);
        String str6 = writer6.toString();
        
        Assert.assertEquals(str1, str3);
        Assert.assertEquals(str1, str5);
        Assert.assertEquals(str2, str4);
        Assert.assertEquals(str2, str6);
    }
}
