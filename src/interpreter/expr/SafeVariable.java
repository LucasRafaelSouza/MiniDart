package interpreter.expr;

import interpreter.value.Value;
import interpreter.util.Utils;

public class SafeVariable extends Variable {

    private boolean initialized;
    private Value<?> value;

    public SafeVariable(int line, String name, boolean constant) {
        super(line, name, constant);
        this.initialized = false;
        this.value = null;
    }

    public Value<?> expr() {
        if (!initialized && (value != null))
        Utils.abort(super.getLine());

    return value;
        //throw new RuntimeException("implementar expr (SafeVariable)");
    }

    public void setValue(Value<?> value) {
        if ((initialized && super.isConstant()) || (value == null) )
            Utils.abort(super.getLine());

        this.value = value;
        this.initialized = true;
        //throw new RuntimeException("implementar setValue (SafeVariable)");
    }
}
