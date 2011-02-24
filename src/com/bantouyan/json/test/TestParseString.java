package com.bantouyan.json.test;

import java.io.IOException;

import com.bantouyan.json.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;

/**
 * 测试解析Json文本过程中产生的各种异常。
 */
public class TestParseString
{
    @Test(expected = JsonParseException.class)
    public void textIsBlank() throws IOException, JsonParseException
    {
        String jsonText = "  ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot parse blank character sequence to json."));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void textIllegalBegin() throws IOException, JsonParseException
    {
        String jsonText = "  begin{}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found json object begin sign '{'"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void textIllegalEnd_object() throws IOException, JsonParseException
    {
        String jsonText = "  {} end";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Non-blank character found at position "));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void textIllegalEnd_array() throws IOException, JsonParseException
    {
        String jsonText = "  [null] end";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Non-blank character found at position "));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectElementNameRepeat() throws IOException, JsonParseException
    {
        String jsonText = "{name: 'v', name:'v2'}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Object element name "));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNotEnd_WithEOF_HaveElement() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, name2: false , ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object end sign \'}\' at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNotEnd_WithEOF_NoElement() throws IOException, JsonParseException
    {
        String jsonText = "{  ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object end sign \'}\' at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementName_Colon() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, : false , ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element name at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementName_Comma() throws IOException, JsonParseException
    {
        String jsonText = "{, , ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element name at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementName_EndObj() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, }";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element name at position"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void objectNoElementName_IllegalChar() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, +}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element name at position"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void objectIllegalElementName_IllegalChar() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, _-ss}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Illegal character found in non-quotation mark string"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void objectIllegalElementName_JsKey() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, class: 'object'}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Non-quotation mark string "));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectIllegalElementName_Sufix_char() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, name2 'v2': 'object'}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Non-blank character found at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectIllegalElementName_Sufix_EOF() throws IOException, JsonParseException
    {
        String jsonText = "{name1: true, 'name2'";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found character \'"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementValue_IllegalCharA() throws IOException, JsonParseException
    {
        String jsonText = "{name1: *, }";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element value at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementValue_IllegalCharB() throws IOException, JsonParseException
    {
        String jsonText = "{name1: www, }";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element value at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementValue_Colon() throws IOException, JsonParseException
    {
        String jsonText = "{name1: :, }";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element value at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectNoElementValue_Comma() throws IOException, JsonParseException
    {
        String jsonText = "{name1: , }";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found object element value at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectIllegalElementValue_SUFX() throws IOException, JsonParseException
    {
        String jsonText = "{name1: 'V2' ss} ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Non-blank  character found at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void objectIllegalElementValue_EOF() throws IOException, JsonParseException
    {
        String jsonText = "{name1: 'V2' ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found end character \'"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void arrayNotEnd_WithEOF_HaveElement() throws IOException, JsonParseException
    {
        String jsonText = "[ 1, true , ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found array end sign \']\' at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void arrayNotEnd_WithEOF_NoElement() throws IOException, JsonParseException
    {
        String jsonText = "[  ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found array end sign \']\' at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void arrayNoValue_Comma() throws IOException, JsonParseException
    {
        String jsonText = "[ 12, ,] ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found array element at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void arrayNoValue_EndAry() throws IOException, JsonParseException
    {
        String jsonText = "[ null , ] ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found array element at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void arrayInvalidValue_Sufix() throws IOException, JsonParseException
    {
        String jsonText = "[ 'v1', 'v2', 'v3' 'v4'] ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Non-blank  character found at position "));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void arrayInvalidValue_EOF() throws IOException, JsonParseException
    {
        String jsonText = "[ 'v1', 'v2', 'v3' ";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found end character \'"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void string_Less0X20() throws IOException, JsonParseException
    {
        String jsonText = "{name: \"value \u0007 value\"}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Character less then \\u0020 found at position"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void string_IllegalUnicodeEscapeA() throws IOException, JsonParseException
    {
        String jsonText = "['String\\us0ss0String']";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Un expected character found in unicode character sequence"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void string_IllegalUnicodeEscapeB() throws IOException, JsonParseException
    {
        String jsonText = "['String\\us000LString']";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Un expected character found in unicode character sequence"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void string_IllegalEscageCharacterA() throws IOException, JsonParseException
    {
        String jsonText = "['String\\U0303String']";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Unexpected escape sign \'\\"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void string_IllegalEscageCharacterB() throws IOException, JsonParseException
    {
        String jsonText = "['String\\x0303String']";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Unexpected escape sign \'\\"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void string_NotEndS() throws IOException, JsonParseException
    {
        String jsonText = "[null, true, 90.3, \"uu\", 'String 79, {}]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found string end quotation"));
            throw e;
        }
    }

    @Test(expected = JsonParseException.class)
    public void string_NotEndD() throws IOException, JsonParseException
    {
        String jsonText = "[null, true, 90.3, \"uu\", \"String 79, {}]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot found string end quotation \'"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_LeadingZero() throws IOException, JsonParseException
    {
        String jsonText = "[-00.4]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Number not allow leading zero at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_HeadPlus() throws IOException, JsonParseException
    {
        String jsonText = "[+40]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Json number cannot begin with '+' at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_AfterMinus() throws IOException, JsonParseException
    {
        String jsonText = "[-.4]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number charcter after '-' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_AfterDot() throws IOException, JsonParseException
    {
        String jsonText = "[-234.E]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after '.' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_After_e() throws IOException, JsonParseException
    {
        String jsonText = "[-234.3eL]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after 'e' or 'E' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_After_eplus() throws IOException, JsonParseException
    {
        String jsonText = "[-234.3e+L]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after 'e' or 'E' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_After_eminus() throws IOException, JsonParseException
    {
        String jsonText = "[-234.3e-L]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after 'e' or 'E' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_After_E() throws IOException, JsonParseException
    {
        String jsonText = "[-234.3EL]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after 'e' or 'E' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_After_Eplus() throws IOException, JsonParseException
    {
        String jsonText = "[-234.3E+L]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after 'e' or 'E' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_NoDigit_After_Eminus() throws IOException, JsonParseException
    {
        String jsonText = "[-234.3E-L]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("In number character after 'e' or 'E' is not 0..9 at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void number_Sufix() throws IOException, JsonParseException
    {
        String jsonText = "[359, -234.3E-23L, 230]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Number invalid sufix at position"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_true_invalidCase() throws IOException, JsonParseException
    {
        String jsonText = "[tRue}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot foun constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_true_invalidChar() throws IOException, JsonParseException
    {
        String jsonText = "[tsue]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot foun constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_true_sufix() throws IOException, JsonParseException
    {
        String jsonText = "[true34]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Invalid sufix of constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_false_invalidCase() throws IOException, JsonParseException
    {
        String jsonText = "[faLse}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot foun constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_false_invalidChar() throws IOException, JsonParseException
    {
        String jsonText = "[falss]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot foun constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_false_sufix() throws IOException, JsonParseException
    {
        String jsonText = "[false34]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Invalid sufix of constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_null_invalidCase() throws IOException, JsonParseException
    {
        String jsonText = "[nuLL}";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot foun constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_null_invalidChar() throws IOException, JsonParseException
    {
        String jsonText = "[nWll]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Cannot foun constant"));
            throw e;
        }
    }
    
    @Test(expected = JsonParseException.class)
    public void constant_null_sufix() throws IOException, JsonParseException
    {
        String jsonText = "[null34]";
        System.out.println("--------------------------------------");
        System.out.print("Text:" + jsonText + "$EOF\nText:01234567890123456789\n");
        try
        {
            Json json = Json.parseJsonText(jsonText);
        }
        catch(JsonParseException e)
        {
            String msg = e.getMessage();
            System.out.println("Exception: " + msg);
            Assert.assertEquals(0, msg.indexOf("Invalid sufix of constant"));
            throw e;
        }
    }
}
