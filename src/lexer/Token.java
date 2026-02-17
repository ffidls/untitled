package lexer;



public class Token {
    public enum Type {
        NUMBER, VARIABLE, PLUS, MINUS, MULTIPLY, DIVIDE, LPAREN, RPAREN
    }

    public enum GroupsType {
        SIGN, PAREN, NUMBER, VARIABLE
    }

    public final Type type;
    public final String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Token[%s, '%s']", type, value);
    }
}