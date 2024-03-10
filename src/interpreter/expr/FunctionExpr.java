package interpreter.expr;

import java.util.Scanner;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import interpreter.util.Utils;
import interpreter.value.BoolValue;
import interpreter.value.ListValue;
import interpreter.value.MapValue;
import interpreter.value.NumberValue;
import interpreter.value.TextValue;
import interpreter.value.Value;

public class FunctionExpr extends Expr {

    private FunctionOp op;
    private Expr expr;

    private static Scanner input = new Scanner(System.in);

    public FunctionExpr(int line, FunctionOp op, Expr expr) {
        super(line);

        this.op = op;
        this.expr = expr;
    }

    @Override
    public Value<?> expr() {
        Value<?> v = expr.expr();

        switch (op) {
            case READ:
                return readOp(v);
            case RANDOM:
                return randomOp(v);
            case LENGTH:
                return lengthOp(v);
            case KEYS:
                return keysOp(v);
            case VALUES:
                return valuesOp(v);
            case TOBOOL:
                return toBoolOp(v);
            case TOINT:
                return toIntOp(v);
            case TOSTR:
                return toStrOp(v);
            default:
                Utils.abort(super.getLine());
                return null;
        }
    }

    private TextValue readOp(Value<?> v) {
        System.out.print(v);

        String text = input.nextLine().trim();
        return text.isEmpty() ? null : new TextValue(text);
    }

    private NumberValue randomOp(Value<?> v) {
        Random gerador = new Random();
        if (!(v instanceof NumberValue)){
            Utils.abort(super.getLine());
        }
        NumberValue  nv = (NumberValue) v;
        int valor = nv.value();
        int resultado = gerador.nextInt(valor);
        return new NumberValue(resultado);
    }

    private NumberValue lengthOp(Value<?> v) {
        if(!(v instanceof ListValue)){
            Utils.abort(super.getLine());
        }
        ListValue l = (ListValue) v;
        List<Value<?>> lista = l.value();
        
        int lenght = lista.size();

        return new NumberValue(lenght); 

    }

    private ListValue keysOp(Value<?> v) {
        if(!(v instanceof MapValue)){
            Utils.abort(super.getLine());
        }
        List<Value<?>> lista = new ArrayList<Value<?>>();
        MapValue map = (MapValue) v;
        Map<Value<?>, Value<?>> m = map.value();

        for(Value<?> valor : m.keySet()){
            lista.add(valor);
        }

        return new ListValue(lista);        
    }


    private ListValue valuesOp(Value<?> v) {
        if(!(v instanceof MapValue)){
            Utils.abort(super.getLine());
        }
        List<Value<?>> lista = new ArrayList<Value<?>>();
        MapValue map = (MapValue) v;
        Map<Value<?>, Value<?>> m = map.value();
        

        for(Entry<Value<?>, Value<?>> entrada : m.entrySet()){
            lista.add(entrada.getValue());
        }

        return new ListValue(lista);        
    
    }
//fazer o valuesop
    private BoolValue toBoolOp(Value<?> v) {
        boolean n;
        if (v == null) {
            n = false;
        } else if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();

            n = b;
        } else if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            int num = nv.value();
            if(num == 0){
                n = false;
            }else{
                n = true;
            }
        } else if (v instanceof TextValue) {
            TextValue sv = (TextValue) v;
            String s = sv.value();
            if(s == null){
                n = false;
            }else {
                n = true;
            }
        } else if(v instanceof MapValue){
            MapValue mv = (MapValue) v;
             if(mv.value() == null){
                n = false;
             } else {
                n = true;
             } 
            }else if(v instanceof ListValue){
                ListValue lv = (ListValue) v;
                if(lv.value() == null){
                    n = false;
                } else{
                    n = true;
                }
            } else{
                n = false;
            }

        

        return new BoolValue(n);
    }
        


    private NumberValue toIntOp(Value<?> v) {
        int n;
        if (v == null) {
            n = 0;
        } else if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();

            n = b ? 1 : 0;
        } else if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            n = nv.value();
        } else if (v instanceof TextValue) {
            TextValue sv = (TextValue) v;
            String s = sv.value();

            try {
                n = Integer.parseInt(s);
            } catch (Exception e) {
                n = 0;
            }
        } else {
            n = 0;
        }

        return new NumberValue(n);
    }

    private TextValue toStrOp(Value<?> v) {
        String n = "";
        String aux = "[";
        if (v == null) {
            n = "null";
        } else if (v instanceof BoolValue) {
            BoolValue bv = (BoolValue) v;
            boolean b = bv.value();

            n = b ? "true" : "false";
            return new TextValue(n);
        } else if (v instanceof TextValue) {
            TextValue sv = (TextValue) v;
            String s = sv.value();
            n = s;
            return new TextValue(n);
    
        } else if (v instanceof ListValue) {
        	
        	ListValue lv = (ListValue) v;
        	List<Value<?>> l = lv.value();
        	int tamanho = l.size();
        	for(int i = 0; i< tamanho; i++) {
        		
        		Value<?> valor = l.get(i);
        		TextValue tv = toStrOp(valor);
        		String s = tv.value();
        		if(i == 0) {
        			aux += s;
        		}else {
        		
        			aux += ", ";
        		
        			aux += s;
        		}
        	}
        	aux += "]";
        	n = aux;
        	return new TextValue(n);
        } else if(v instanceof MapValue) {
            MapValue mapv = (MapValue) v;
            Map<Value<?>, Value<?>> mapa = mapv.value();

            int a = 0;
            n += "{";

            for(Value<?> chave : mapa.keySet()){
                TextValue schave = toStrOp(chave);
                Value<?> valor = mapa.get(chave);
                TextValue sValor = toStrOp(valor);

                String c = schave.value();
                String val = sValor.value();
                if(a == 0) {
                    n += c;
                    n += ":";
                    n += val;
                    a++;
                } else {
                    n += ", ";
                    n += c;
                    n += ":";
                    n += val;
                }

            }
            n += "}";

            return new TextValue(n);
        }
        else if (v instanceof NumberValue) {
        	//System.out.println("entrou  number");
            NumberValue nv = (NumberValue) v;
            int i = nv.value();
            n = Integer.toString(i);
            return new TextValue(n);
        }
        
        return null; 
        
    }

}
