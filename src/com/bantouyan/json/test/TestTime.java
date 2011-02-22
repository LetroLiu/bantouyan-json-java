package com.bantouyan.json.test;

import java.io.IOException;

import com.bantouyan.json.Json;
import com.bantouyan.json.JsonArray;
import com.bantouyan.json.JsonObject;
import com.bantouyan.json.JsonParseException;

import org.junit.Test;
import org.junit.Assert;

public class TestTime
{
    @Test
    public void test() throws IOException, JsonParseException
    {
        String str = "[-1, 10.3, 33e4, true, false, null,"
            + "{INT: -1, FLOAT: 22.4, BOOL: true, \"NULL\": null,"
            + "\"array\":[],"
            + "\"object\":{}"
            + "},[null, \"Error\", {}, []]"
            + "]";
        Json jsona = Json.parseJsonText(str);
        JsonArray jsonb = new JsonArray();
    
        JsonArray aryNull = new JsonArray();
        JsonObject objNull = new JsonObject();
        
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
        
        int k = 6;
        int rptNum = 1000 * 1000; 
        if(k == 0)
        {
            for(int i=0; i<rptNum; i++)
            {
                String sa = jsona.generateJsonText();
                String sb = jsonb.generateJsonText();
                Assert.assertEquals(sa, sb);
            }
        }
        else if(k == 1)
        {
            for(int i=0; i<rptNum; i++)
            {
                Assert.assertTrue(jsona.equals(jsonb));
            }
        }
        else if(k == 2)
        {
            for(int i=0; i<rptNum; i++)
            {
                int ha = jsona.hashCode();
                int hb = jsonb.hashCode();
            }
        }
        else if(k == 3)
        {
            for(int i=0; i<rptNum; i++)
            {
                int ha = jsona.toString().hashCode();
                int hb = jsonb.toString().hashCode();
            }
        } 
        if(k == 4)
        {
            jsonb.getJsonObject(6).add("empty");
            jsonb.append();
            for(int i=0; i<rptNum; i++)
            {
                String sa = jsona.generateJsonText();
                String sb = jsonb.generateJsonText();
                Assert.assertFalse(sa.equals(sb));
            }
        }
        else if(k == 5)
        {
            jsonb.getJsonObject(6).add("empty");
            jsonb.append();
            for(int i=0; i<rptNum; i++)
            {
                Assert.assertFalse(jsona.equals(jsonb));
            }
        }

    }

}
