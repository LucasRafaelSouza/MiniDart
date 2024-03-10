package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.Value;

import java.util.List;

public class ForCommand extends Command {
    /*Completar o for command
     * Pensar a respeito do in
     *
     */
    private Variable  var;
    private Expr expr;
    private Command cmds;

    public ForCommand(int line, Variable var, Expr expr, Command cmds) {
        super(line);
        this.var = var;
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute() {
        Value<?> value = expr.expr();

        if (!(value instanceof ListValue)){
            Utils.abort(super.getLine());
        }//conferir se o valor retornado por expr() é uma listvalue
        ListValue v  = (ListValue) value;
        List<Value<?>> x = v.value();
        int tamanho = x.size();
        for(int i =0; i<tamanho; i++){

            var.setValue(x.get(i));
            this.cmds.execute();
        }
    }
}

