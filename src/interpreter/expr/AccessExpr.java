package interpreter.expr;

import java.util.List;
import java.util.Map;

import interpreter.util.Utils;
import interpreter.value.ListValue;
import interpreter.value.MapValue;
import interpreter.value.NumberValue;
import interpreter.value.Value;

public class AccessExpr extends SetExpr {

    private SetExpr base;
    private Expr index;

    public AccessExpr(int line, SetExpr base, Expr index) {
        super(line);
        this.base = base;
        this.index = index;
    }

    @Override
    public Value<?> expr() {
        Value<?> bvalue = base.expr();
        if (bvalue instanceof ListValue) {
            //throw new RuntimeException("implementar p/ lista");
            ListValue mv = (ListValue) bvalue;
            List<Value<?>> x = mv.value();
            Integer indi = null;
            Value<?> ivalue =  index.expr();
            if(ivalue instanceof NumberValue){//assumindo que o indice deve ser inteiro
                indi =  ((NumberValue) ivalue).value();
            }
            System.out.println(indi);
            if (ivalue == null)
                Utils.abort(super.getLine());

            return x.get(indi);

        } else if (bvalue instanceof MapValue) {
            MapValue mv = (MapValue) bvalue;
            Map<Value<?>, Value<?>> map = mv.value();

            Value<?> ivalue = index.expr();
            if (ivalue == null)
                Utils.abort(super.getLine());

            return map.get(ivalue);
        } else {
            Utils.abort(super.getLine());
        }

        return null;
    }

    @Override
    public void setValue(Value<?> value) {
        Value<?> bvalue = base.expr();
        if (bvalue instanceof ListValue) {
            //throw new RuntimeException("implementar p/ lista");
            ListValue mv = (ListValue) bvalue;
            List<Value<?>> x = mv.value();
            Integer indi = null;
            Value<?> ivalue =  index.expr();
            if(ivalue instanceof NumberValue){//assumindo que o indice deve ser inteiro
                indi =  ((NumberValue) ivalue).value();
            }

            if (ivalue == null)
                Utils.abort(super.getLine());

            x.set(indi,value);

        } else if (bvalue instanceof MapValue) {
            MapValue mv = (MapValue) bvalue;
            Map<Value<?>, Value<?>> map = mv.value();

            Value<?> ivalue = index.expr();
            if (ivalue == null)
                Utils.abort(super.getLine());

            map.put(ivalue, value);
        } else {
            Utils.abort(super.getLine());
        }
    }
    
}
