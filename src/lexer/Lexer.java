package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token.GroupsType lastType = null;

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            Token.GroupsType currentType = null;
            Token newToken = null;

            if (Character.isDigit(c)) {
                newToken = readNumber();
                currentType = Token.GroupsType.NUMBER;
            } else if (Character.isLetter(c)) {
                newToken = readVariable();
                currentType = Token.GroupsType.VARIABLE;
            } else if (c == '+') {
                newToken = new Token(Token.Type.PLUS, "+");
                currentType = Token.GroupsType.SIGN;
                pos++;
            } else if (c == '-') {
                newToken = new Token(Token.Type.MINUS, "-");
                currentType = Token.GroupsType.SIGN;
                pos++;
            } else if (c == '*') {
                newToken = new Token(Token.Type.MULTIPLY, "*");
                currentType = Token.GroupsType.SIGN;
                pos++;
            } else if (c == '/') {
                newToken = new Token(Token.Type.DIVIDE, "/");
                currentType = Token.GroupsType.SIGN;
                pos++;
            } else if (c == '(') {
                newToken = new Token(Token.Type.LPAREN, "(");
                currentType = Token.GroupsType.PAREN;
                pos++;
            } else if (c == ')') {
                newToken = new Token(Token.Type.RPAREN, ")");
                currentType = Token.GroupsType.PAREN;
                pos++;
            } else {
                throw new RuntimeException("Unexpected symbol: " + c);
            }

            checkSyntax(lastType, currentType, newToken, tokens.isEmpty());

            tokens.add(newToken);
            lastType = currentType;
        }

        if (lastType == Token.GroupsType.SIGN) {
            throw new IllegalArgumentException("Error: expression cannot end with a sign");
        }

        return tokens;
    }

    private void checkSyntax(Token.GroupsType lastType, Token.GroupsType currentType, Token currentToken, boolean isFirst) {
        if (isFirst && currentType == Token.GroupsType.SIGN) {
            if (currentToken.type != Token.Type.MINUS) {
                throw new IllegalArgumentException("Error: expression cannot start with " + currentToken.value);
            }
        }

        if (lastType != null) {
            if (lastType == Token.GroupsType.SIGN && currentType == Token.GroupsType.SIGN) {
                throw new IllegalArgumentException("Error: double signs");
            }

            if ((lastType == Token.GroupsType.NUMBER || lastType == Token.GroupsType.VARIABLE) &&
                    (currentType == Token.GroupsType.NUMBER || currentType == Token.GroupsType.VARIABLE)) {
                throw new IllegalArgumentException("Error: missing operator between operands");
            }

            if (lastType == Token.GroupsType.SIGN && currentToken.type == Token.Type.RPAREN) {
                throw new IllegalArgumentException("Error: sign before closing parenthesis");
            }

            if (lastType == Token.GroupsType.PAREN && (currentType == Token.GroupsType.NUMBER || currentType == Token.GroupsType.VARIABLE)) {
            }
        }
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return new Token(Token.Type.NUMBER, sb.toString());
    }

    private Token readVariable() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return new Token(Token.Type.VARIABLE, sb.toString());
    }
}