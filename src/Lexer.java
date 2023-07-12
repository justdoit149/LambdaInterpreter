import java.util.regex.Pattern;

public class Lexer {//词法分析
    public String source;
    public int index;
    public TokenType token;
    public String tokenvalue;
    /**
     * Lexer 类内构造函数
     * @param s
     */
    public Lexer(String s){//参数s为初始的Lambda表达式（字符串形式）
        index = 0;
        source = s;
        nextToken();
    }

    /**
     * 解析函数
     * @return TokenType 当前合法的token的tokenType
     */
    public TokenType nextToken(){
        char c;
        String pattern1 = "\\s";//空白符
        String pattern2 = "[a-z]";
        String pattern3 = "[a-zA-Z]";
        do{
            c = nextChar();

        }while (Pattern.matches(pattern1, c+""));//跳过空白符
        switch (c){
            case '\\': token = TokenType.LAMBDA; tokenvalue = null; break;
            case '.': token = TokenType.DOT; tokenvalue = null; break;
            case '(': token = TokenType.LPAREN; tokenvalue = null; break;
            case ')': token = TokenType.RPAREN; tokenvalue = null; break;
            case '\0': token = TokenType.EOF; tokenvalue = null; break;
            default:
                if(Pattern.matches(pattern2, c+"")) {//是否匹配pattern2
                    String s = "";
                    do {
                        s+=c;//identifier 小写开头 允许大小写混用
                        c = nextChar();
                    } while (Pattern.matches(pattern3, c + ""));
                    index--;
                    token = TokenType.LCID;
                    tokenvalue = s;
                    //System.out.println(s);
                }else {
                    System.out.println("Wrong Input");
                }
        }
//        System.out.println(token);
        return token;
    }

    public char nextChar(){
        if(index>= source.length()){
            return '\0';
        }
        return source.charAt(index++);
    }
    /**
     * check token == t 检查类型
     * @param t
     * @return 类型是否匹配
     */
    public boolean nextIsMatched(TokenType t){
        if(token == t) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 保证当前token的类型与传入的t相同，并解析下一个符合此法规则的token
     * 如果解析到不同于t的类型，则退出并报错
     * @param t
     */
    public void checkAndNext(TokenType t){
        assert(token == t);//保证当前token的类型与传入的t相同
        nextToken();
    }

    /**
     * 跳过当前TokenType t，并解析下一个符合此法规则的token
     * @param t
     * @return 是否skip成功
     */
    public boolean skipThisType(TokenType t){
        if(token == t) {
            nextToken();
            return true;
        }
        else {
            return false;
        }
    }
}
