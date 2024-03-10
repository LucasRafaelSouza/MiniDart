package interpreter.expr;

import java.util.ArrayList;
import java.util.List;
import interpreter.value.Value;

public class SingleListItem extends ListItem{
    Expr expr;
    

    public SingleListItem(int line, Expr expr){
        super(line);
        this.expr = expr;
    }

    @Override
    public List<Value<?>> items() {
        Value<?> v = expr.expr();
        List<Value<?>> lista = new ArrayList<Value<?>>();

        lista.add(v);

        return lista;

    }

}
