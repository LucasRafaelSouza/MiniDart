package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ListExpr extends Expr {
    private List<ListItem> list;
    public ListExpr(int line) {
        super(line);
        list = new ArrayList<ListItem>();
    }
    public void addItem(ListItem item) {
        list.add(item);
    }

    public Value<?> expr() {
        List<Value<?>> v = new ArrayList<Value<?>>();
        for (ListItem l : list) {
            if (l instanceof SingleListItem || l instanceof SpreadListItem|| l instanceof ListItem){
                List<Value<?>> valor = l.items();//fazer para os dois listitem
                if (valor == null)
                    Utils.abort(super.getLine());
                v.addAll(valor);//adiciona todos os elementos da lista
            }
        }
        ListValue a =  new ListValue(v);
        return a;
    }


}
