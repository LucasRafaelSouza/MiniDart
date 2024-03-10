package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

import interpreter.value.Value;

public class SpreadListItem  extends ListItem {
    Expr expr;


    public SpreadListItem(int line, Expr expr){
        super(line);
        this.expr = expr;
    }

    @Override
    public List<Value<?>> items() {
        //pelo que eu entendi, esse é o caso do [...ls] que adiciona todos os elementos da lista ls
        Value<?> v = this.expr.expr();
        List<Value<?>> lista2 = (List<Value<?>>) v.value();
        List<Value<?>> lista = new ArrayList<Value<?>>();

        lista.addAll(lista2);

        return lista;
        

    }
}
