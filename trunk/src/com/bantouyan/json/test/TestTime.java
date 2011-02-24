package com.bantouyan.json.test;

import java.io.IOException;

import com.bantouyan.json.Json;
import com.bantouyan.json.JsonArray;
import com.bantouyan.json.JsonObject;
import com.bantouyan.json.JsonParseException;
import com.bantouyan.json.JsonPrimitive;
import com.bantouyan.json.Json.JsonType;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;

public class TestTime
{
    private Json json = null;
    private int cnt = 1000 * 1 * 1;
       
    @Ignore
    @Before
    public void init() throws Exception
    {
        String strAry = "[1, 2, 3, true, false, null, 99.99, 'string']";
        String strObj = "{n1:true, n2: false, n3: null, n4: 30, n5:99.99, n6: 'string'}";
        
        JsonArray jaryA = (JsonArray)Json.parseJsonText(strAry);
        JsonArray jaryB = (JsonArray)Json.parseJsonText(strAry);
        JsonArray jaryC = (JsonArray)Json.parseJsonText(strAry);
        JsonObject jobjA = (JsonObject)Json.parseJsonText(strObj);
        JsonObject jobjB = (JsonObject)Json.parseJsonText(strObj);
        JsonObject jobjC = (JsonObject)Json.parseJsonText(strObj);
        
        jobjA.add("listA", jaryA);
        jaryA.insert(0, jobjB);
        jaryA.append(jobjB);
        jobjA.add("listB", jaryB);
        jaryB.insert(0, jobjC);
        jaryB.append(jobjC);
        jobjB.add("aryB", jaryB);
        jobjB.add("aryC", jaryC);
//        jobjC.add("aryB", jaryA);
        jobjC.add("aryC", jaryC);
//        jaryC.append(jobjA);
        
        JsonArray ary = new JsonArray();
        for(int i=0; i<10; i++)
        {
            ary.append(jobjA);
        }
        json = ary;
    }
    
    @Ignore
    @Test
    public void generateTextTime()
    {
        String str = null;
        for(int i=0; i<cnt; i++)
        {
            str = json.generateJsonText(true);
        }
        System.out.println(str);
    }
    

    @Test
    public void parseTextTime() throws IOException, JsonParseException
    {
        for(int i=0; i< cnt; i++)
        {
            Json json = Json.parseJsonText(str);
        }
    }
    
    public static void main(String[] args){}
    
    String str = "[{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"},{\"n1\":true,\"listB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n5\":99.99,\"n4\":30,\"listA\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"aryB\":[{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"},1,2,3,true,false,null,99.99,\"string\",{\"n1\":true,\"aryC\":[1,2,3,true,false,null,99.99,\"string\"],\"n5\":99.99,\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n4\":30,\"n3\":null,\"n2\":false,\"n6\":\"string\"}],\"n3\":null,\"n2\":false,\"n6\":\"string\"}]";
}
