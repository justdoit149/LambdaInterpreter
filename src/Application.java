

import java.util.ArrayList;

public class Application extends AST{//应用（两个Lambda项）
    //形式：(lam1 lam2)，或多个Lambda项(x y1 y2 y3 ……)，理解为((x) (y1 y2 y3 ……))
    //这样拆成左右树，右树仍然是应用的话需要继续递归处理。
    AST lhs;//左树
    AST rhs;//右树

    Application(AST l, AST s){
        lhs = l;
        rhs = s;
    }

    public String toString(){
        //编译器认为调用的是作为AST(父类)的toString()方法
        //实际运行时是看rhs实际是什么类型来调用相应的子类的toString()方法。
        if (lhs == null) return rhs.toString();
        else if(rhs == null) return lhs.toString();
        else if(lhs!= null && rhs!= null) return "("+lhs.toString()+" "+rhs.toString()+")";
        else return "";
    }

    @Override
    public boolean equals(AST ast) {
        if (ast instanceof Application) {//instanceof用于类型判断
            if (this.lhs.equals(((Application) ast).lhs) && this.rhs.equals(((Application) ast).rhs))
                return true;
            else
                return false;
        }else
            return false;
    }

}
