package com.bantouyan.json;

import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;

/**
 * 内部类，用来将Reader或String解析成Json类实例，仅供此Json库内部使用。
 * 
 * @author 飞翔的河马
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
    private int ch = -1; // the current char of the reader
    private int pos = -1; // the current position of the reader
//    private char c; // the variant c used to debug, so needn't it
    
    /**
     * 用Reader新建一个JsonTextParser对象。
     * @param reader 提供Json字符流
     * @throws IOException 读写Reader发生异常
     */
    public JsonTextParser(Reader reader) throws IOException
    {
        this.reader = reader;
        next();
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
        next();
    }
    
    /**
     * 根据reader内容解析成JsonObject或JsonArray。
     * @return 解析后的JsonObject或JsonArray实例
     * @throws IOException 读取reader有误
     * @throws JsonException Json格式错误（不是JsonObject或JsonArray）
     */
    public Json parse() throws IOException, JsonException
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
                String msg = "Cannot found json object begin sign '{'" +
                		" or json array begin sign ']' at position " + pos + ".";
                throw new JsonException(msg);
            }
            
            next();
        }
        if(json == null)
        {
            String msg = "Cannot parse blank character sequence to json.";
            throw new JsonException(msg);
        }
        
        return json;
    }
    
    /**
     * 从当前字符开始解析JsonObject实例，
     * 进入时pos指向字符'{'，退出时指向对应的'}'之后的第一个字符。
     * @return 对应的JsonObject实例
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析成JsonObject，或Name出现重复）
     */
    private JsonObject parseObject() throws IOException, JsonException
    {
        JsonObject json = new JsonObject();
        boolean needNextElement = false;
        
        next(); //skip character '{'
        
        while(ch != -1)
        {
            if(needNextElement == false && ch == '}') break;
            
            if(isBlankCharacter(ch))
            {
                next(); //skip blank character
            }
            else
            {
                String name = parseName();
                if (json.containsName(name))
                {
                    String msg = "Object element name \"" + name
                            + "\" at position " + pos + " is repeated.";
                    throw new JsonException(msg);
                } 
                else
                {
                    Json value = parseValue('}');
                    parseTailBlank(',', '}');
                    json.set(name, value); //已经检测过Name是否重复，所以不使用方法add
                }
                
                if (ch == '}') //子元素后是'}'，JsonObject结束
                {
                    break;
                } else
                //子元素后是','，需解析下一个子元素（Name Value对）
                {
                    next(); // skip character ','
                    needNextElement = true;
                }
            }
        }
        
        if(ch == '}')
        {
            next(); // skip character '}'
        }
        else
        {
            String msg = "Cannot found object end sign \'}\' at position " + pos + ".";
            throw new JsonException(msg);
        }
        
        return json;
    }
    
    /**
     * 从当前字符开始解析JsonArray实例，
     * 进入时pos指向字符'['，退出时指向对应的']'之后的第一个字符。
     * @return 对应的JsonArray实例
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析成JsonArray）
     */
    private JsonArray parseArray() throws IOException, JsonException
    {
        JsonArray json = new JsonArray();  
        boolean needNextElement = false;  
        
        next(); // skip character '['
        
        while(ch != -1)
        {
            if(needNextElement == false && ch == ']') break;
            
            if (isBlankCharacter(ch))
            {
                next(); //skip blank character
            }
            else
            {
                Json value = parseValue(']');
                json.append(value);
                parseTailBlank(',', ']');
                if (ch == ']') //子元素后是']'，数组结束
                {
                    break;
                } 
                else //子元素后是','，需解析下一个子元素
                {
                    next(); // skip character ','
                    needNextElement = true;
                }
            }
        }
        
        if(ch == ']')
        {
            next(); // skip character ']'
        }
        else
        {
            String msg = "Cannot found array end sign \']\' at position " + pos + ".";
            throw new JsonException(msg);
        }
        
        return json;
    }
    
    /**
     * 解析JsonObject子元素的name部分，进入时指向name部分（可包括前导空白）的第一个字符，
     * 退出时指向字符':'后的第一个字符。
     * @return 表示name的String
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析出一个表示Name的字符串或无法找到结束字符“:”）
     */
    private String parseName() throws IOException, JsonException
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
                    String msg = "Cannot found object element name at position " + pos + ".";
                    throw new JsonException(msg);
                }
            }
            
            next();
        }
        
        if(ch == ':') //Name部分正常结束
        {
            next(); //skip character ':'
        }
        else
        {
            String msg = "Cannot found object element name at position " + pos + ".";
            throw new JsonException(msg);
        }
        
        return str;
    }
    
    /**
     * 解析Json子元素（对JsonObject而言指子元素的Value部分），
     * 进入时指向表示value（可包含前导空白）的第一个字符，
     * 退出时指向value（不包含尾空白）之后的第一个字符。
     * @param endChar 子元素除逗号外的结束符，只允许是]或}
     * @return Json子元素的实例
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析成Json子元素）
     */
    private Json parseValue(int endChar) throws IOException, JsonException
    {
        Json json = null;
        
        while(ch != -1)
        {
            if(isBlankCharacter(ch))
            {
                next(); // skip blank character
            }
            else if(ch == '{')
            {
                json = parseObject();
                break;
            }
            else if(ch == '[')
            {
                json = parseArray();
                break;
            }
            else if(ch == 't') // parse true
            {
                json = parseJsonConstant("true", trueAry, endChar);
                break;
            }
            else if(ch == 'f') //parse false
            {
                json = parseJsonConstant("false", falseAry, endChar);
                break;
            }
            else if(ch == 'n') //parse null
            {
                json = parseJsonConstant("null", nullAry, endChar);
                break;
            }
            else if(ch == '\'' || ch == '\"')
            {
                String str = parseString(ch);
                json = new JsonPrimitive(str);
                break;
            }
            else if(ch == '-' || (ch >= '0' && ch<= '9'))
            {
                Number num = parseNumber(endChar);
                json = new JsonPrimitive(num);
                break;
            }
            else 
            {
                String msg = null;
                if(ch == '+')
                {
                    next();
                    if(ch>='0' && ch<='9')
                    {
                            msg = "Json number cannot begin with '+' at position " + pos + ".";
                    }
                }
                
                if(msg == null)
                {
                    msg = (endChar == ']')? "Cannot found array element at position ":
                                            "Cannot found object element value at position ";
                    msg += pos + ".";
                }
                throw new JsonException(msg);
            }
            
        }
        
        return json;
    }
    
    /**
     * 解析不带引号的字符串（用于处理JsonObject子元素的Name部分），
     * 进入时pos指向字符串的开头，退出时指向字符串的下一个字符。
     * @return 所解析的字符串
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析出一个不带引号的String）
     */
    private String parseString() throws IOException, JsonException
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
                String msg = "Illegal character found in non-quotation mark string at position " + pos
                           + ", so cannot as object element name.";
                throw new JsonException(msg);
            }
            
            next();
        }
        
        String str = build.toString();
        if(isJsKeywords(str))
        {
            String msg = "Non-quotation mark string \"" + str + "\" found at position " + pos 
                       + " is javascript keywords, so cannot as object element name..";
            throw new JsonException(msg);
        }
        return str;
    }
    
    /**
     * 解析带引号的字符串，进入时pos指向开头的引号，退出时指向结尾的引号的下一个字符。
     * @param quoteChar 字符串所使用的引号，' or "
     * @return 所解析的字符串
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析出一个带引号的字符串）
     */
    private String parseString(int quoteChar) throws IOException, JsonException
    {
        StringBuilder build = new StringBuilder();
        next(); // skip quatorChar
        
        while(ch != -1)
        {
            if(ch == quoteChar) break;
            
            if(ch < 0x0020)
            {
                String msg = "Character less then \\u0020 found at position " + pos + ".";
                throw new JsonException(msg);
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
                                throw new JsonException(msg);
                            }
                            
                        }
                        ch = Integer.parseInt(new String(unicodeChar), 16);
                        break;
                    default:
                        String msg = "Unexpected escape sign \'\\" + (char)ch + "\' found at position " + pos + ".";
                        throw new JsonException(msg);
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
            String msg = "Cannot found string end quotation ";
            msg += (quoteChar == '"')? "\'" + (char)quoteChar + "\'": 
                                       "\"" + (char)quoteChar + "\"";
            msg += " at position " + pos + ".";
            throw new JsonException(msg);
        }
        
        return build.toString();
    }
    
    /**
     * 解析Number字符串， 进入时pos指向Number的第一个字符，退出时指向Number的下一个字符。
     * @param endChar Number后除空白、逗号外可接受的终止符，只允许是']' 或 '}'
     * @return Number对象（Long或Double类型）
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（无法解析成一个Json Number）
     */
    private Number parseNumber(int endChar) throws IOException, JsonException
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
        if(ch == '0') //begin with 0
        {
            build.append((char)ch);
            next();
            if(ch >= '0' && ch <= '9')
            {
                throw new JsonException("Number not allow leading zero at position " + pos + ".");
            }
        }
        else if(ch > '0' && ch <= '9') //begin with 1..9
        {
            build.append((char)ch);
            next();
            
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
        }
        else
        {
            throw new JsonException("In number charcter after '-' is not 0..9 at position " + pos + ".");
        }
        
        //parse fraction
        if(ch == '.')
        {
            build.append((char)ch);
            isInt = false;            
            next(); //skip character '.'
            if(ch>='0' && ch<='9')
            {
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
            else
            {
                throw new JsonException("In number character after '.' is not 0..9 at position " + pos + ".");
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
            
            if(ch>='0' && ch<='9')
            {
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
            else
            {
                throw new JsonException("In number character after 'e' or 'E' is not 0..9 at position " + pos + ".");
            }
        }
        if(ch != ',' && ch != endChar && !isBlankCharacter(ch))
        {
            String msg = "Number invalid sufix at position " + pos + ".";
            throw new JsonException(msg);
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
            throw new JsonException(msg, e);
        }
    }
    
    /**
     * 解析Json常量true、false与null，进入时指向Json常量的第一个字符，退出时指向常量的下一个字符。
     * @param constName 只允许是true、false与null
     * @param constAry 只允许使用JsonTextParser静态类常量trueAry、falseAry与nullAry
     * @param endChar Json常量后后除空白、逗号外可接受的终止符，只允许是']' 或 '}'
     * @return 对应的Json常量
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（非Json常量字符串，或常量字符串后紧跟着非法字符）
     */
    private JsonPrimitive parseJsonConstant(String constName, int[] constAry, int endChar)
    throws IOException, JsonException
    {
        JsonPrimitive json = null;
        
        for(int i=0; i<constAry.length; i++)
        {
            if(ch != constAry[i])
            {
                String msg = "Cannot foun constant \"" + constName + "\" at position " + pos + ".";
                throw new JsonException(msg);
            }
            
            next();
        }
        if(ch != ',' && ch != endChar && !isBlankCharacter(ch))
        {
            String msg = "Invalid sufix of constant \"" + constName + "\" at position " + pos + ".";
            throw new JsonException(msg);
        }
        
        json = (constAry == trueAry)? Json.trueJson: 
                    (constAry == falseAry)? Json.falseJson: Json.nullJson;
        
        return json;
    }
    
    /**
     * 从当前字符开始解析剩余的空白字符串，遇到终止字符前出现其他字符或EOF为异常。
     * 进入时pos指向空白字符，或endsChar中的字符，
     * 退出时指向遇到的第一个endsChar字符。
     * @param endChar1 终止字符，不能是-1（-1表示Reader已到达尾部，即EOF）
     * @param endChar2 终止字符，不能是-1（-1表示Reader已到达尾部，即EOF）
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（遇到终止字符前读到非空白字符或已到达Reader尾部）
     */
    private void parseTailBlank(int endChar1, int endChar2) throws IOException, JsonException
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
                String msg = "Non-blank  character found at position " + pos 
                        + " before '" + (char)endChar1 + "' and '" + (char)endChar2 + "'.";
                throw new JsonException(msg);
            }
            
            next();
        }
        
        if(! found)
        {
            String msg = "Cannot found end character \'" + (char)endChar1 + "\' and \'" + (char)endChar2 + "\' at position " + pos + ".";
            throw new JsonException(msg);
        }        
    }
    
    /**
     * 从当前字符开始解析剩余的空白字符串，遇到终止字符前出现其他字符或EOF为异常。
     * 进入时pos指向空白字符，或endsChar中的字符，
     * 退出时指向遇到的第一个endsChar字符。
     * @param endChar 可以用-1表示期望到达Reader尾部，即EOF
     * @throws IOException 读取Reader发生异常
     * @throws JsonException Json格式不正确（遇到终止字符前读到非空白字符，或当终止字符不是-1时已到达Reader尾部）
     */
    private void parseTailBlank(int endChar) throws IOException, JsonException
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
                String msg = "Non-blank character found at position " + pos + " before ";
                msg += (endChar == -1)? "EOF.": "'" + (char)endChar + "'.";
                throw new JsonException(msg);
            }
            
            next();
        }
        
        if(! found && endChar != -1)
        {
            String msg = "Cannot found character \'" + (char)endChar + "\'(end char) at position " + pos + ".";
            throw new JsonException(msg);
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
//        System.out.print((char)ch);
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
     * 将字符串以Josn文本（带引号与转义符）的形式追到字符流末尾。
     * @param str 要追加的字符串
     * @param dest 接受字符串的字符流对象
     * @throws IOException 发生IO异常
     */
    protected static void jsonStringToAppendable(String str, Appendable dest) throws IOException
    {
        dest.append('\"');
        int len = str.length();
        for(int i=0; i<len; i++)
        {
            char c = str.charAt(i);
            if(c == '\'')
            {
                dest.append("\\\'");
            }
            else if(c == '\"')
            {
                dest.append("\\\"");
            }
            else if(c == '\\')
            {
                dest.append("\\\\");
            }
            else if(c == '/')
            {
                dest.append("\\/");
            }
            else if(c == '\b')
            {
                dest.append("\\b");
            }
            else if(c == '\f')
            {
                dest.append("\\f");
            }
            else if(c == '\n')
            {
                dest.append("\\n");
            }
            else if(c == '\r')
            {
                dest.append("\\r");
            }
            else if(c == '\t')
            {
                dest.append("\\t");
            }
            else if(c <= 0x1F)
            {
                dest.append("\\u");
                dest.append(String.format("%04x", (int)c));
            }
            else
            {
                dest.append(c);
            }
        }
        dest.append('\"');
    }
    
    /**
     * 将字符串尽量以不带引号的形式追加到字符流末尾，如果无法转换为不带引号的字符串，
     * 则以带引号的字符串输出。
     * @param str 要追加的字符串
     * @param dest 接受字符串的字符流对象
     * @throws IOException 发生IO异常
     */
    protected static void jsonStringToAppendableWithoutQutoe(String str, Appendable dest) throws IOException
    {
        boolean canToNoquote = true;

        int len = str.length();
        if(len ==0)
        {
            canToNoquote = false;
        }
        else
        {
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
        }
        
        if(canToNoquote)
        {
            canToNoquote = ! isJsKeywords(str);
        }
        
        if(canToNoquote)
        {
            dest.append(str);
        }
        else
        {
            jsonStringToAppendable(str, dest);
        }
    }
}
