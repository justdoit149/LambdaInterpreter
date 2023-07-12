import java.util.ArrayList;

public class Parser {//语法分析，形成语法树
    //这里合理设计Term、Application和Atom这三个方法是关键，它三个相互调用，应该明确每一个能干什么。
    Lexer lexer;
    public Parser(Lexer l){
        lexer = l;
    }

    public AST parse(){//解析入口。这里的ArrayList参数是为了看变量是“第几层”。
        AST ast = parseAsTerm(new ArrayList<>());
        return ast;
    }

    /**
     *解析 term
     * @param ctx
     * @return
     */
    private AST parseAsTerm(ArrayList<String> ctx){
        String param;
        String paramValue;
        if (lexer.skipThisType(TokenType.LAMBDA)){
            //开头就是“\”那就用Term。
            if(lexer.nextIsMatched(TokenType.LCID)) {
                param = lexer.tokenvalue;
                lexer.checkAndNext(TokenType.LCID);
                if (lexer.skipThisType(TokenType.DOT)) {
                    ctx.add(0,param);
                    //从前面插入，后面的会依次后移，用来表示深度
                    paramValue = ""+ctx.indexOf(param);
                    AST aTerm = parseAsTerm(ctx);
                    ctx.remove(ctx.indexOf(param));
                    //用完了再删掉
                    return new Abstraction(new Identifier(param,paramValue),aTerm);
                }
            }
        }else {
            //常规来说，是 (\x.y) 这样外面有一个大括号的。那先交给Application
            return parseAsApplication(ctx);
        }
        return null;
    }

    /**
     *解析 application
     * @param ctx
     * @return
     */
    private AST parseAsApplication(ArrayList<String> ctx){
        //应用看成>=2个Atom组成，这里Atom是广义的，(……)属于Atom
        AST lhs = parseAsAtom(ctx);
        AST rhs;
        while(true) {
            rhs = parseAsAtom(ctx);
            //对于上述 (\x.y) 这种，只有一个Atom，实质上也不是Application，就直接让App的右树为null即可
            if (rhs==null) {
                return lhs;
            } else {
                lhs = new Application(lhs,rhs);
                //对于多个Atom的应用，从第二个开始从前往后依次作为rhs，然后这个rhs前面的所有都是lhs
                //这样知道运行到最后把所有Atom处理完。
            }
        }
    }

    /**
     *解析 atom
     * @param ctx
     * @return
     */
    private AST parseAsAtom(ArrayList<String> ctx){
        String param;
        String paramValue;
        //还是 (\x.y) 这个式子，Atom会先去掉括号，然后把剩下的东西作为Term处理
        //也就是把\x.y丢给Term
        if(lexer.skipThisType(TokenType.LPAREN)){
            AST aTerm = parseAsTerm(ctx);
            if(lexer.skipThisType(TokenType.RPAREN))
                return aTerm;
        }else if(lexer.nextIsMatched(TokenType.LCID)){
            //这是单个原子的情况。
            param = lexer.tokenvalue;
            paramValue = ""+ctx.indexOf(param);
            lexer.checkAndNext(TokenType.LCID);
            return new Identifier(param,paramValue);
        }
        return null;
    }
}
