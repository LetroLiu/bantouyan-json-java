package com.bantouyan.json;

import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;

/**
 * 内部类，用来将Reader或String解析成Json类实例，仅供此Json库内部使用。
 * @author bantouyan
 * @version 1.00
 */
class JsonTextParser
{
    private static String[] jsKeywords = {"abstract", "boolean", "break", 
        "byte", "case", "catch", "char", "class", "const", "continue",
        "debugger", "default", "delete", "do", "double", "else", "enum",
        "export", "extends", "fasle", "final", "finally", "float", "for",
        "function", "goto", "if", "implements", "import", "in", "instanceof",
        "int", "interface", "long", "native", "new", "null", "package",
        "private", "protected", "public", "return", "short", "static",
        "super", "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "true", "typeof", "var", "void", "volatile",
        "while", "with"};
    private static int[] trueAry = {'t', 'r', 'u', 'e'};
    private static int[] falseAry =  {'f', 'a', 'l', 's', 'e'};
    private static int[] nullAry = {'n', 'u', 'l', 'l'};
    private char[] unicodeChar = new char[4];
    
    private Reader reader;
    // after JsonTextParser object create, ch is the first character and pos equal 0
    private int ch; // the current char of the reader
    private int pos; // the current position of the reader
//    private char c; // the variant c used to debug, so needn't it
    
    /**
     * 用Reader新建一个JsonTextParser对象。
     * @param reader 提供Json字符流
     * @throws IOException 读写Reader发生异常
     */
    public JsonTextParser(Reader reader) throws IOException
    {
        this.reader = reader;
        ch = this.reader.read();
        pos = 0;
    }
    
    /**
     * 用字符串新建一个JsonTextParser对象。
     * @param text Json文本
     * @throws IOException 读取Reader（内部使用）发生异常
     */
    public JsonTextParser(String text)throws IOException
    {
        StringReader strReader = new StringReader(text);
        
        this.reader = strReader;
        ch = this.reader.read();
        pos = 0;
    }
    
    /**
     * 
     * 根据reader内容解析成JsonObject或JsonArray。
     * @return 解析后的JsonObject或JsonArray
     * @throws IOException 读取reader有误
     * @throws JsonParseException 不符合Json格式
     */
    public Json parse() throws IOException, JsonParseException
    {
        Json json = null;
        
        while(ch != -1)
        {
            if(ch == '{')
            {
                json = parseObject();
                parseTailBlank(-1);
            }
            else if(ch == '[')
            {
                json = parseArray();
                parseTailBlank(-1);
            }
            else if(! isBlankCharacter(ch))
            {
                String msg = "Non-blank character found at position " + pos + ".";
                throw new JsonParseException(msg);
            }
            
            next();
        }
        
        return json;
    }
    
    /**
     * 从当前字符开始解析JsonObject实例，
     * 进入时pos指向字符'{'，退出时指向对应的'}'之后的第一个字符。
     * @return 对应的JsonObject实例
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析成JsonObject）或Name出现重复
     */
    private Json parseObject() throws IOException, JsonParseException
    {
        JsonObject json = new JsonObject();
        
        next(); //skip character '{'
        
        while(ch != -1)
        {
            if(ch == '}') break;
            
            String name = parseName();
            Json value = parseValue('}');
            if(json.containsName(name))
            {
                String msg = "Name \"" + name + "\" at position " + pos + " is repeated.";
                throw new JsonParseException(msg);
            }
            else
            {
                json.add(name, value);
            }
            parseTailBlank(',', '}');
            if(ch == '}') break;
            
            next(); // skip character ','
        }
        
        if(ch == '}')
        {
            next(); // skip character '}'
        }
        else
        {
            String msg = "Cannot found object end sign \'}\' at position " + pos + ".";
            throw new JsonParseException(msg);
        }
        
        return json;
    }
    
    /**
     * 从当前字符开始解析JsonArray实例，
     * 进入时pos指向字符'['，退出时指向对应的']'之后的第一个字符。
     * @return 对应的JsonArray实例
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析成JsonArray）
     */
    private Json parseArray() throws IOException, JsonParseException
    {
        JsonArray json = new JsonArray();        
        next(); // skip character '['
        
        while(ch != -1)
        {
            if(ch == ']') break;
            
            Json value = parseValue(']');
            json.append(value);
            
            parseTailBlank(',', ']');
            if(ch == ']') break;

            next(); // skip character ','
        }
        
        if(ch == ']')
        {
            next(); // skip character ']'
        }
        else
        {
            String msg = "Cannot found array end sign \']\' at position " + pos + ".";
            throw new JsonParseException(msg);
        }
        
        return json;
    }
    
    /**
     * 解析Json Object的name部分，进入时指向name部分（包括前导空白）的第一个字符，
     * 退出时指向字符':'后的第一个字符。
     * @return 表示name的String
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析出一个表示Name的字符串或无法找到结束字符“:”）
     */
    private String parseName() throws IOException, JsonParseException
    {
        String str = null;
        
        while(ch != -1)
        {
            if(ch == '\'' || ch == '\"')
            {
                str = parseString(ch);
                parseTailBlank(':');
                break;
            }
            else if(! isBlankCharacter(ch))
            {
                //无引号字符串
                if(ch =='_' || ch == '$' || (ch >= 'a' && ch <= 'z')
                        || (ch >= 'A' && ch <= 'Z') || ch > 256)
                {
                    str = parseString();
                    parseTailBlank(':');
                    break;
                }
                else
                {
                    String msg = "Non-blank character found at position " + pos + ".";
                    throw new JsonParseException(msg);
                }
            }
            
            next();
        }
        
        if(ch == ':')
        {
            next();
        }
        else
        {
            String msg = "Cannot found object name part at position " + pos + ".";
            throw new JsonParseException(msg);
        }
        
        return str;
    }
    
    /**
     * 解析子Json实例， 包括 JsonObject、JsonArray以及JsonPrimitive value，
     * 进入时指向表示value（包含前导空白）的第一个字符，
     * 退出时指向value（可不包含尾空白）之后的第一个字符。
     * @return Json实例
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析成Json实例）
     */
    private Json parseValue(int endChar) throws IOException, JsonParseException
    {
        Json json = null;
        
        while(ch != -1)
        {
            if(ch == '{')
            {
                json = parseObject();
                parseTailBlank(',', endChar);
                break;
            }
            else if(ch == '[')
            {
                json = parseArray();
                parseTailBlank(',', endChar);
                break;
            }
            else if(ch == 't') // parse true
            {
                for(int i=0; i<trueAry.length; i++)
                {
                    if(ch != trueAry[i])
                    {
                        String msg = "Cannot foun \"true\" at position " + pos + ".";
                        throw new JsonParseException(msg);
                    }
                    
                    next();
                }
                
                json = Json.trueJson;
                parseTailBlank(',', endChar);
                break;
            }
            else if(ch == 'f') //parse false
            {
                for(int i=0; i<falseAry.length; i++)
                {
                    if(ch != falseAry[i])
                    {
                        String msg = "Cannot foun \"false\" at position " + pos + ".";
                        throw new JsonParseException(msg);
                    }
                    
                    next();
                } 
                
                json = Json.falseJson;
                parseTailBlank(',', endChar); 
                break;
            }
            else if(ch == 'n') //parse null
            {
                for(int i=0; i<nullAry.length; i++)
                {
                    if(ch != nullAry[i])
                    {
                        String msg = "Cannot foun \"null\" at position " + pos + ".";
                        throw new JsonParseException(msg);
                    }
                    
                    next();
                } 
                
                json = Json.nullJson;
                parseTailBlank(',', endChar); 
                break;
            }
            else if(ch == '\'' || ch == '\"')
            {
                String str = parseString(ch);
                json = new JsonPrimitive(str);
                parseTailBlank(',', endChar);
                break;
            }
            else if(ch == '-' || (ch >= '0' && ch<= '9'))
            {
                Number num = parseNumber();
                json = new JsonPrimitive(num);
                parseTailBlank(',', endChar);
                break;
            }
            else if(! isBlankCharacter(ch))
            {
                String msg = "Non-blank character found at position " + pos + ".";
                throw new JsonParseException(msg);
            }
            
            next(); // skip blank character
        }
        
        return json;
    }
    
    /**
     * 解析不带引号的字符串，进入时pos指向字符串的开头，退出时指向字符串的下一个字符。
     * @return 所解析的字符串
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析出一个不带引号的String）
     */
    private String parseString() throws IOException, JsonParseException
    {
        StringBuilder build = new StringBuilder();
        
        while(ch != -1)
        {
            if(ch == ':' || isBlankCharacter(ch)) break;

            if(ch > 256 || ch =='_' || ch == '$' || (ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch<= '9'))
            {
                build.append((char)ch);
            }
            else
            {
                String msg = "Illegal character found in non-quotation mark string at position " + pos + ".";
                throw new JsonParseException(msg);
            }
            
            next();
        }
        
        String str = build.toString();
        if(isJsKeywords(str))
        {
            String msg = "Non-quotation mark string \"" + str + "\" found at position " + pos + " is javascript keywords.";
            throw new JsonParseException(msg);
        }
        return str;
    }
    
    /**
     * 解析带引号的字符串，进入时pos指向开头的引号，退出时指向结尾的引号的下一个字符。
     * @param quoteChar 字符串所使用的引号，' or "
     * @return 所解析的字符串
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析出一个带引号String）
     */
    private String parseString(int quoteChar) throws IOException, JsonParseException
    {
        StringBuilder build = new StringBuilder();
        next(); // skip quatorChar
        
        while(ch != -1)
        {
            if(ch == quoteChar) break;
            
            if(ch < 0x0020)
            {
                String msg = "Character less then \\u0020 found at position " + pos + ".";
                throw new JsonParseException(msg);
            }
            if(ch == '\\')
            {
                next();
                switch(ch)
                {
                    case '\"':
                        ch = '\"';
                        break;
                    case '\'':
                        ch = '\'';
                        break;
                    case '\\':
                        ch = '\\';
                        break;
                    case '/':
                        ch = '/';
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case 'u':
                        for(int i=0; i<4; i++)
                        {
                            next();
                            if((ch >='0' && ch <='9') || (ch >= 'a' && ch <='f')
                                    || (ch>= 'A' && ch <= 'F'))
                            {
                                unicodeChar[i] = (char)ch;
                            }
                            else
                            {
                                String msg = "Un expected character found in unicode character sequence at positon " + pos + ".";
                                throw new JsonParseException(msg);
                            }
                            
                        }
                        ch = Integer.parseInt(new String(unicodeChar), 16);
                        break;
                    default:
                        String msg = "Unexpected escape sign \'" + (char)ch + "\' found at position " + pos + ".";
                        throw new JsonParseException(msg);
                }
            }
            build.append((char)ch);

            next();
        }
        
        if(ch == quoteChar)
        {
            next(); // skip quator char
        }
        else
        {
            String msg = "Cannot found string end quotation \'" + quoteChar + "\' at position " + pos + ".";
            throw new JsonParseException(msg);
        }
        
        return build.toString();
    }
    
    /**
     * 解析Number字符串， 进入时pos指向Number的第一个字符，退出时指向Number的下一个字符
     * @return Number对象（Long或Double类型）
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（无法解析成一个Json Number）
     */
    private Number parseNumber() throws IOException, JsonParseException
    {
        StringBuilder build = new StringBuilder();
        boolean isInt = true;
        
        // parse minus sign
        if(ch == '-')
        {
            build.append((char)ch);
            next();
        }
        
        //parse integer part
        while(ch != -1)
        {
            if(ch >= '0' && ch <= '9')
            {
                build.append((char)ch);
            }
            else
            {
                break;
            }
            
            next();
        }
        
        //parse fraction
        if(ch == '.')
        {
            build.append((char)ch);
            isInt = false;            
            next(); //skip character '.'
            
            while(ch != -1)
            {
                if(ch>='0' && ch<='9')
                {
                    build.append((char)ch);
                }
                else
                {
                    break;
                }
                
                next();
            }
            
        }
        
        // parse exponent
        if(ch == 'e' || ch == 'E')
        {
            build.append((char)ch);
            isInt = false;
            
            next(); //skip character e
            
            //parse plus or minus sign
            if(ch == '+' || ch == '-')
            {
                build.append((char)ch);
                next();
            }
            
            while(ch != -1)
            {
                if(ch>='0' && ch<='9')
                {
                    build.append((char)ch);
                }
                else
                {
                    break;
                }

                next();
            }
        }
        
        String numStr = build.toString();
        try
        {
            if(isInt)
            {
                return Long.parseLong(numStr);
            }
            else
            {
                return Double.parseDouble(numStr);
            }
        } 
        catch (NumberFormatException e)
        {
            String msg = (isInt)? "Integer ": "Float ";
            msg += "string \"" + numStr + "\" format error at position " + pos + ".";
            throw new JsonParseException(msg, e);
        }
    }
    
    /**
     * 从当前字符开始解析剩余的空白字符串，遇到终止字符前出现其他字符或EOF为异常。
     * 进入时pos指向空白字符，或endsChar中的字符，
     * 退出时指向遇到的第一个endsChar字符。
     * @param endChar1 终止字符，不能是-1（-1表示Reader已到达尾部，即EOF）
     * @param endChar2 终止字符，不能是-1（-1表示Reader已到达尾部，即EOF）
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（遇到终止字符前读到非空白字符或已到达Reader尾部）
     */
    private void parseTailBlank(int endChar1, int endChar2) throws IOException, JsonParseException
    {
        boolean found = false;
        
        while(ch != -1)
        {
            if(ch == endChar1 || ch == endChar2)
            {
                found = true;
                break;
            }
            else if(! isBlankCharacter(ch))
            {
                String msg = "Non-blank character found at position " + pos + ".";
                throw new JsonParseException(msg);
            }
            
            next();
        }
        
        if(! found)
        {
            String msg = "Cannot found end character \'" + endChar1 + "\' and \'" + endChar2 + "\' at position " + pos + ".";
            throw new JsonParseException(msg);
        }        
    }
    
    /**
     * 从当前字符开始解析剩余的空白字符串，遇到终止字符前出现其他字符或EOF为异常。
     * 进入时pos指向空白字符，或endsChar中的字符，
     * 退出时指向遇到的第一个endsChar字符。
     * @param endChar 可以用-1表示期望到达Reader尾部，即EOF
     * @throws IOException 读取Reader发生异常
     * @throws JsonParseException Json格式不正确（遇到终止字符前读到非空白字符，或当终止字符不是-1时已到达Reader尾部）
     */
    private void parseTailBlank(int endChar) throws IOException, JsonParseException
    {
        boolean found = false;
        
        while(ch != -1)
        {
            if(ch == endChar)
            {
                found = true;
                break;
            }
            else if(! isBlankCharacter(ch))
            {
                String msg = "Non-blank character found at position " + pos + ".";
                throw new JsonParseException(msg);
            }
            
            next();
        }
        
        if(! found && endChar != -1)
        {
            String msg = "Cannot found character \'" + endChar + "\' at position " + pos + ".";
            throw new JsonParseException(msg);
        }
    }
    
    /**
     * 检测字符c是否属于空白字符（空格、回车、换行、制表符）。
     * @param c 被检测字符
     * @return 是空白返回true，否则返回false
     */
    private boolean isBlankCharacter(int c)
    {
        return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n')? true: false;
    }
    
    /**
     * 读取下一个字符，并更新ch与pos。
     * @throws IOException 读取Reader发生异常
     */
    private void next() throws IOException
    {
        ch = reader.read();
//        c = (char)ch; //this line used for debug
        pos++;
    }
    
    /**
     * 判断字符串是否为JavaScript关键字或保留字。
     * @param str 被判断的字符串
     * @return 是JavaScript关键字或保留字返回true，否则返回false
     */
    protected static boolean isJsKeywords(String str)
    {
        for(String key: jsKeywords)
        {
            if(str.equals(key)) return true;
        }
        return false;
    }
    
    /**
     * 将字符串转换为带双引号的Json字符串。
     * @param str 源字符串
     * @return 转换后的字符串
     */
    protected static String toJsonString(String str)
    {
        String rtnValue = "";
        StringBuilder build = new StringBuilder();
        
        build.append('\"');
        int len = str.length();
        for(int i=0; i<len; i++)
        {
            char c = str.charAt(i);
            if(c == '\'')
            {
                build.append("\\\'");
            }
            else if(c == '\"')
            {
                build.append("\\\"");
            }
            else if(c == '\\')
            {
                build.append("\\\\");
            }
            else if(c == '/')
            {
                build.append("\\/");
            }
            else if(c == '\b')
            {
                build.append("\\b");
            }
            else if(c == '\f')
            {
                build.append("\\f");
            }
            else if(c == '\n')
            {
                build.append("\\n");
            }
            else if(c == '\r')
            {
                build.append("\\r");
            }
            else if(c == '\t')
            {
                build.append("\\t");
            }
            else if(c <= 0x1F)
            {
                build.append("\\u");
                build.append(String.format("%04x", (int)c));
            }
            else
            {
                build.append(c);
            }
        }
        build.append('\"');
        rtnValue = build.toString();
        
        return rtnValue;
    }
    
    /**
     * 将字符串转换为不带引号的Json字符串，如果无法成功，则转换为带引号的Json字符串。
     * @param str 源字符串
     * @return 转换后的字符串
     */
    protected static String toJsonNoquoteString(String str)
    {
        String rtnValue = "";
        boolean canToNoquote = true;

        int len = str.length();
        if(len ==0) return "";
        
        for(int i=0; i<len; i++)
        {
            char c = str.charAt(i);
            
            if(!(c > 256 || c =='_' || c == '$' || (c >= '0' && c<= '9')
                    || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')))
            {
                canToNoquote = false;
                break; 
            }
        }
        
        if(canToNoquote)
        {
            canToNoquote = ! isJsKeywords(str);
        }

        if(canToNoquote)
        {
            rtnValue = str;
        }
        else
        {
            rtnValue = toJsonString(str);
        }
        return rtnValue;
    }
}
