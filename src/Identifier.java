

public class Identifier extends AST {//原子（单个标识符）
    String name; //名字
    String value;//De Bruijn index值，用于beta规约代入时确定代哪个
    //由于\x.x和\y.y本质上是一样的（alpha变换下等价），因此我们只关心变量是第几层的
    //如(\.\.(1 0))实际上就是(\f.\x.f x)，0是最近的，数字越大离得层数越远
    public Identifier(String n, String v){
        name = n;
        value = v;
    }
    public String toString(){
        return value;
    }

    public boolean equals(AST ast){
        if(ast instanceof Identifier) {
            if (this.value.equals(((Identifier) ast).value))
                return true;
            else
                return false;
        }else
            return false;
    }
}
