public class Interpreter {//解释器
    Parser parser;
    AST astAfterParser;

    public Interpreter(Parser p){
        parser = p;
        astAfterParser = p.parse();
//        System.out.println("After parser:"+astAfterParser.toString());
    }

    private  boolean isAbstraction(AST ast){
        return ast instanceof Abstraction;
    }
    private  boolean isApplication(AST ast){
        return ast instanceof Application;
    }
    private  boolean isIdentifier(AST ast){
        return ast instanceof Identifier;
    }

    public AST eval(){
        return evalAST(astAfterParser);
    }


    private  AST evalAST(AST ast){
        while(true) {
            if (isApplication(ast)) {
                if (isAbstraction(((Application) ast).lhs)) {
                    ast = substitute(((Abstraction) ((Application) ast).lhs).body, ((Application) ast).rhs);
                } else if (isApplication(((Application) ast).lhs) && !isIdentifier(((Application) ast).rhs)) {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                    if (isAbstraction(((Application) ast).lhs)) {
                        ast = evalAST(ast);
                    }
                    return ast;
                } else if (isApplication(((Application) ast).lhs) && isIdentifier(((Application) ast).rhs)) {
                    ((Application) ast).lhs = evalAST(((Application) ast).lhs);
                    if (isAbstraction(((Application) ast).lhs)) {
                        ast = evalAST(ast);
                    }
                    return ast;
                } else {
                    ((Application) ast).rhs = evalAST(((Application) ast).rhs);
                    return ast;
                }
            } else if (isAbstraction(ast)) {
                ((Abstraction) ast).body = evalAST(((Abstraction) ast).body);
                return ast;
            } else {
                return ast;
            }
        }
    }

    private AST substitute(AST node,AST value){
        return shift(-1,subst(node,shift(1,value,0),0),0);
    }

    private AST subst(AST node, AST value, int depth){
        if(isApplication(node)){
            Application application = new Application(subst(((Application) node).lhs,value,depth),subst(((Application) node).rhs,value,depth));
            return application;
        }
        else if(isAbstraction(node)) {
            Abstraction abstraction = new Abstraction(((Abstraction) node).param, subst(((Abstraction) node).body, value, depth + 1));
            return abstraction;
        }
        else{
            if(depth == Integer.parseInt(((Identifier)node).value)) {
                return shift(depth,value,0);
            }
            else return node;
        }
    }

    private AST shift(int by, AST node,int from){
        if(isApplication(node)){
            Application application = new Application(shift(by,((Application)(node)).lhs,from), shift(by,((Application)(node)).rhs,from));
            return application;
        }
        else if(isAbstraction(node)){
            Abstraction abstraction = new Abstraction(((Abstraction) node).param,shift(by,((Abstraction) node).body,from+1));
            return abstraction;
        }
        else{
            int v1 = Integer.parseInt(((Identifier) node).value);
            Identifier identifier = new Identifier(((Identifier) node).name, String.valueOf(v1 + (v1 >= from ? by : 0)));
            return identifier;
        }
    }
}