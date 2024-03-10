package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.*;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpr extends Expr {

    private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr(int line, Expr left, BinaryOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> expr() {

        Value<?> v1 = left.expr();
        Value<?> v2 = right.expr();

        switch (op) {
            case IF_NULL:
                return ifNullOp(v1, v2);
            case AND:
                return andOp(v1, v2);
            case OR:
                return orOp(v1, v2);
            case EQUAL:
                return equalOp(v1, v2);
            case NOT_EQUAL:
                return notEqualOp(v1, v2);
            case LOWER_THAN:
                return lowerThanOp(v1, v2);
            case LOWER_EQUAL:
                return lowerEqualOp(v1, v2);
            case GREATER_THAN:
                return greaterThanOp(v1, v2);
            case GREATER_EQUAL:
                return greaterEqualOp(v1, v2);
            case ADD:
                return addOp(v1, v2);
            case SUB:
                return subOp(v1, v2);
            case MUL:
                return mulOp(v1, v2);
            case DIV:
                return divOp(v1, v2);
            case MOD:
                return modOp(v1, v2);
            default:
                Utils.abort(super.getLine());
                return null;
        }
    }

    private Value<?> ifNullOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof TextValue && v2 instanceof TextValue) {
            TextValue nv1 = (TextValue) v1;
            TextValue nv2 = (TextValue) v2;

            String n1 = nv1.value();
            String n2 = nv2.value();
            String res;
            if(n1 == null){
                 res = n2;}
            else {
                res = n1;
            }

            TextValue bres = new TextValue(res);
            return bres;
        }
        else if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            Integer n1 = nv1.value();
            Integer n2 = nv2.value();
            Integer res;
            if(n1 == null){
                res = n2;}
            else {
                res = n1;
            }

            NumberValue bres = new NumberValue(res);
            return bres;
            }else if(v1 == null) {
            	return v2;
            }
        else {
        	//System.out.println(v1);
            Utils.abort(super.getLine());
            return null;
        }

        //throw new RuntimeException("Me implemente");
    }

    private Value<?> andOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof BoolValue && v2 instanceof BoolValue) {
            BoolValue nv1 = (BoolValue) v1;
            BoolValue nv2 = (BoolValue) v2;

            boolean n1 = nv1.value();
            boolean n2 = nv2.value();
            boolean res = n1 & n2;

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
        //throw new RuntimeException("Me implemente");
    }

    private Value<?> orOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof BoolValue && v2 instanceof BoolValue) {
            BoolValue nv1 = (BoolValue) v1;
            BoolValue nv2 = (BoolValue) v2;

            boolean n1 = nv1.value();
            boolean n2 = nv2.value();
            boolean res = n1 | n2;

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
        //throw new RuntimeException("Me implemente");
    }

    private Value<?> equalOp(Value<?> v1, Value<?> v2) {
            //System.out.printf("equal, valor 1: %d, valor 2: %d\n",v1.value(),v2.value() );
        //System.out.println(v1.value() );
        Boolean resultado;
        if((v1 instanceof NumberValue) && (v2 instanceof NumberValue)){
            Integer n1  = ((NumberValue) v1).value();
            Integer n2  = ((NumberValue) v1).value();
            if(n1==n2){
                resultado = true;
            }
        else{
                resultado = false;
            }
        }
        else if (v1 instanceof TextValue && v2 instanceof TextValue){
            String n1 = ((TextValue) v1).value();
            String n2 = ((TextValue) v2).value();
            resultado  = n1.equals(n2);
        }
        else if (v1 instanceof BoolValue && v2 instanceof BoolValue){
            Boolean n1 = ((BoolValue) v1).value();
            Boolean n2 = ((BoolValue) v2).value();
            if(n1==n2){
                resultado = true;
            }
            else{
                resultado = false;
            }
        }
        else if (v1 == null ){
            if(v1==v2){
                resultado = true;
            }
            else{
                resultado = false;
            }
        }
        else if (v2 == null ){
            if(v1==v2){
                resultado = true;
            }
            else{
                resultado = false;
            }
        }
        else{
            //System.out.println((v1 instanceof NumberValue) && (v2 instanceof NumberValue));
            //System.out.println(v2 instanceof NumberValue);
            Utils.abort(super.getLine());
            return null;
        }

        BoolValue res = new BoolValue(resultado);
        return res;
    }

    private Value<?> notEqualOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            boolean res = !(n1 == n2);

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
        //throw new RuntimeException("Me implemente");
    }

    private Value<?> lowerThanOp(Value<?> v1, Value<?> v2) {
        //System.out.println(v1);
        //System.out.println(v2);
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();

            boolean res = n1 < n2;

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> lowerEqualOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            boolean res = n1 <= n2;

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> greaterThanOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            boolean res = n1 > n2;

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> greaterEqualOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            boolean res = n1 >= n2;

            BoolValue bres = new BoolValue(res);
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> addOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            int res = n1 + n2;

            NumberValue nres = new NumberValue(res);
            return nres;
        }
        else if(v1 instanceof TextValue && v2 instanceof TextValue) {
        	TextValue tv1 = (TextValue) v1;
        	TextValue tv2 = (TextValue) v2;
        	
        	String s1 = tv1.value();
        	String s2 = tv2.value();
        	
        	String res = s1.concat(s2);
        	
        	TextValue tres = new TextValue(res);
        	return tres;
        }
        else if(v1 instanceof ListValue && v2 instanceof ListValue) {
            ListValue tv1 = (ListValue) v1;
            ListValue tv2 = (ListValue) v2;

            List<Value<?>> s1 = tv1.value();
            List<Value<?>> s2 = tv2.value();

            List<Value<?>> res =  new ArrayList<Value<?>>();
            res.addAll(s1);
            res.addAll(s2);

            ListValue tres = new ListValue(res);
            return tres;}
        else if(v1 instanceof ListValue ) {
            ListValue tv1 = (ListValue) v1;


            List<Value<?>> s1 = tv1.value();


            List<Value<?>> res =  new ArrayList<Value<?>>();
            res.addAll(s1);
            res.add(v2);

            ListValue tres = new ListValue(res);
            return tres;}
        else if(v2 instanceof ListValue ) {
            ListValue tv2 = (ListValue) v2;


            List<Value<?>> s2 = tv2.value();


            List<Value<?>> res =  new ArrayList<Value<?>>();
            res.addAll(s2);
            res.add(v1);

            ListValue tres = new ListValue(res);
            return tres;}
        else {
            Utils.abort(super.getLine());
            return null;
        }
        //throw new RuntimeException("Me implemente");
    }

    private Value<?> subOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            int res = n1 - n2;

            NumberValue nres = new NumberValue(res);
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> mulOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            int res = n1 * n2;

            NumberValue nres = new NumberValue(res);
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> divOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            if (n2 != 0) {
                int res = n1 / n2;

                NumberValue nres = new NumberValue(res);
                return nres;
            }
        }

        Utils.abort(super.getLine());
        return null;
    }

    private Value<?> modOp(Value<?> v1, Value<?> v2) {
        if (v1 instanceof NumberValue && v2 instanceof NumberValue) {
            NumberValue nv1 = (NumberValue) v1;
            NumberValue nv2 = (NumberValue) v2;

            int n1 = nv1.value();
            int n2 = nv2.value();
            if (n2 != 0) {
                int res = n1 % n2;

                NumberValue nres = new NumberValue(res);
                return nres;
            }
        }
        
        Utils.abort(super.getLine());
        return null;
    }
    
}
