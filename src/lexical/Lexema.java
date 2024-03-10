package lexical;
public class Lexema {

    public String token;
    public TokenType type;

    public Lexema(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

}

