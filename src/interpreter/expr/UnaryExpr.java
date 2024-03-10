package interpreter.expr;

import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.NumberValue;
import interpreter.value.Value;

public class UnaryExpr extends Expr {

    private Expr expr;
    private UnaryOp op;

    public UnaryExpr(int line, Expr expr, UnaryOp op) {
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr() {
        switch (op) {
            case NEG:
                return negOp();
            case NOT:
                return notOp();
            case PRE_INC:
                return preIncOp();
            case POS_INC:
                return posIncOp();
            case PRE_DEC:
                return preDecOp();
            case POS_DEC:
                return posDecOp();
            default:
                Utils.abort(super.getLine());
                return null;
        }
    }

    private Value<?> negOp() {
        Value<?> v = expr.expr();
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            int res = -n;
            NumberValue nres = new NumberValue(res);
            if(expr instanceof SetExpr){
                ((SetExpr) expr).setValue(nres);
            }
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }
    
    private Value<?> notOp() {
        Value<?> v = expr.expr();
        if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();
            boolean res = !b;
            BoolValue bres = new BoolValue(res);
            if(expr instanceof SetExpr){
                ((SetExpr) expr).setValue(bres);
            }
            return bres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }
    
    private Value<?> preIncOp() {
        //throw new RuntimeException("Me implemente!");
        Value<?> v = expr.expr();
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            int res = n + n;
            NumberValue nres = new NumberValue(res);
            if(expr instanceof SetExpr){
                ((SetExpr) expr).setValue(nres);
            }
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }
    
    private Value<?> posIncOp() {
        //throw new RuntimeException("Me implemente!");
        Value<?> v = expr.expr();
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            int res = n + 1;
            //System.out.printf("posinc %d\n",res);
            NumberValue nres = new NumberValue(res);
            if(expr instanceof SetExpr){
                ((SetExpr) expr).setValue(nres);
            }
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }
    
    private Value<?> preDecOp() {
        Value<?> v = expr.expr();
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            int res = n - 1;
            NumberValue nres = new NumberValue(res);
            if(expr instanceof SetExpr){
                ((SetExpr) expr).setValue(nres);
            }
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
        //throw new RuntimeException("Me implemente!");
    }
    
    private Value<?> posDecOp() {
        Value<?> v = expr.expr();
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int n = nv.value();
            int res = n - 1;
            NumberValue nres = new NumberValue(res);
            if(expr instanceof SetExpr){
                ((SetExpr) expr).setValue(nres);
            }
            return nres;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
        //throw new RuntimeException("Me implemente!");
    }
    
}