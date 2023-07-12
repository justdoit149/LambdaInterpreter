

import java.util.ArrayList;

public class Abstraction extends AST {//抽象（Lambda表达式）
    //形式：\x._，其中x为变量，后面“_”代表body部分
    Identifier param;//变量
    AST body;//表达式
    Abstraction(Identifier p, AST b){
        param = p;
        body = b;
    }
    public String toString(){
        return "\\"+"."+body.toString();
    }
    @Override
    public boolean equals(AST ast) {
        if (ast instanceof Abstraction) {
            if (this.body.equals(((Abstraction) ast).body))
                return true;
            else
                return false;
        }else
            return false;
    }
}
