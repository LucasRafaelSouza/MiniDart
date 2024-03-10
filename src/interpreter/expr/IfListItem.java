package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.Value;

public class IfListItem extends ListItem {
    Expr expr;
    ListItem thenItem;
    ListItem elseItem;


    public IfListItem(int line, Expr expr, ListItem thenItem, ListItem elseItem ){
        super(line);
        this.expr = expr;
        this.thenItem = thenItem;
        this.elseItem = elseItem;
    }

    @Override
    public List<Value<?>> items() {
        List<Value<?>> lista = new ArrayList<Value<?>>();
        Value<?> v = expr.expr();
        if(!(v instanceof BoolValue)){
            Utils.abort(super.getLine());
        }
        BoolValue bv = (BoolValue) v;
        boolean b = bv.value();

        if(b){
           lista = thenItem.items();
        }else if(elseItem != null){
            lista = elseItem.items();
        }
        return lista;
    }
}
