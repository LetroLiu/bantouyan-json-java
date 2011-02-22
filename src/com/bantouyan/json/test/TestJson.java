package com.bantouyan.json.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.bantouyan.json.Json;
import com.bantouyan.json.JsonArray;
import com.bantouyan.json.JsonObject;
import com.bantouyan.json.JsonParseException;

public class TestJson
{
    @Test
    public void jsonEquals() throws IOException, JsonParseException
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
}
