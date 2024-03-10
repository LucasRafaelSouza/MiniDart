package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.ListValue;
import interpreter.value.Value;

import java.util.ArrayList;
import java.util.List;

public class ForListItem extends  ListItem{
    Expr expr;
    Variable var;
    ListItem Item;


    public ForListItem(int line, Expr expr,Variable var, ListItem Item ){
        super(line);
        this.expr = expr;
        this.Item = Item;
        this.var = var;
    }

    @Override
    public List<Value<?>> items() {
        Value<?> value = expr.expr();

        if (!(value instanceof ListValue)){
            Utils.abort(super.getLine());

        }//conferir se o valor retornado por expr() é uma listvalue*/
        List<Value<?>> y =  new ArrayList<Value<?>>();
        List<Value<?>> x =  new ArrayList<Value<?>>();
       // Value<?> value2 = expr.expr();
        ListValue lista = (ListValue) value;
        
        List<Value<?>> valores = lista.value();
        
        for(Value<?> varia : valores) {
        	var.setValue(varia);
        	 y = (List<Value<?>>) Item.items();
        	 for(Value<?> v2 : y) {
        		 x.add(v2);
        	 }
        }
        
        //List<Value<?>> x = (List<Value<?>>) Item.items();
       // int tamanho = x.size();
       //{
            //Var.setValue(x.get(i));
            //value = expr.expr();
            //System.out.println(value);
        	
            
            //criar um novo valor de Expr const

            /*if (cmds instanceof ListValue)){

            }*/

        //}
        return x;
    }
}
