package interpreter.command;

import interpreter.expr.Expr;
import interpreter.util.Utils;
import interpreter.value.BoolValue;
//import interpreter.value.TextValue;
import interpreter.value.Value;


public class AssertCommand extends  Command{
    private Expr expr;
    private Expr msg;

    public AssertCommand(int line, Expr expr, Expr msg) {
        super(line);
        this.expr = expr;
        this.msg = msg;
    }

    public void execute() {
        Value<?> x = expr.expr();

        if (!(x instanceof BoolValue)){
            Utils.abort(super.getLine());}
        BoolValue bv = (BoolValue) x;
        boolean b = bv.value();
        if(!b){
            System.out.println(msg.expr());
            //exit(-1);
        }
        //System.out.println(expr.expr());
    }
}
