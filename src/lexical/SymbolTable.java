package lexical;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<String, TokenType>();
        //criar tabaela de simoblos com o put
        st.put("?", TokenType.NULLABLE);
        st.put(";", TokenType.SEMICOLON);
        st.put("=", TokenType.ASSIGN);
        st.put("==", TokenType.EQUAL);
        st.put("!=", TokenType.NOT_EQUAL);
        st.put("<", TokenType.LOWER);
        st.put("<=", TokenType.LOWER_EQUAL);
        st.put(">", TokenType.GREATER);
        st.put(">=", TokenType.GREATER_EQUAL);
        st.put("+", TokenType.ADD);
        st.put("-", TokenType.SUB);
        st.put("*", TokenType.MUL);
        st.put("/", TokenType.DIV);
        st.put("%", TokenType.MOD);
        st.put("[", TokenType.SQUARE_BRACKETS_L);
        st.put("]", TokenType.SQUARE_BRACKETS_R);
        st.put(",", TokenType.COMMA);
        st.put("{", TokenType.CURLY_BRACKETS_L);
        st.put("}", TokenType.CURLY_BRACKETS_R);
        st.put("&&", TokenType.AND);
        st.put("||", TokenType.OR);
        st.put("??", TokenType.IF_NULL);
        st.put("!", TokenType.DENIAL);
        st.put("--", TokenType.DECREMENT);
        st.put("++", TokenType.INCREMENT);
        st.put("var", TokenType.VAR);
        st.put("var?", TokenType.VARN);
        st.put("final", TokenType.FINAL);
        st.put("assert", TokenType.ASSERT);
        st.put("print", TokenType.PRINT);
        st.put("while", TokenType.WHILE);
        st.put("do?", TokenType.DO);
        st.put("for", TokenType.FOR);
        st.put("read", TokenType.READ);
        st.put("random", TokenType.RANDOM);
        st.put("length", TokenType.LENGTH);
        st.put("keys", TokenType.KEYS);
        st.put("values", TokenType.VALUES);
        st.put("in", TokenType.IN);
        st.put("tobool", TokenType.TOBOOL);
        st.put("toint", TokenType.TOINT);
        st.put("tostr", TokenType.TOSTR);
        st.put("null", TokenType.NULL);
        st.put("false", TokenType.FALSE);
        st.put("true", TokenType.TRUE);
        st.put(":", TokenType.TWO_POINTS);
        st.put("...", TokenType.THREE_POINT);
        st.put("(", TokenType.PARENTESES_L);
        st.put(")", TokenType.PARENTESES_R);
        st.put("if", TokenType.IF);
        st.put("else", TokenType.ELSE);
        

    }

    public boolean contains(String token) {
        return st.containsKey(token);
    }

    public TokenType find(String token) {
        return this.contains(token) ? st.get(token) : TokenType.NAME;
    }
}
