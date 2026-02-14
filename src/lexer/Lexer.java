package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;
    private int parenthesisBalance = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token.GroupsType lastGroup = null;
        Token.Type lastType = null;

        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            Token newToken;
            Token.GroupsType currentGroup;

            if (Character.isDigit(c)) {
                newToken = readNumber();
                currentGroup = Token.GroupsType.NUMBER;
            }
            else if (Character.isLetter(c)) {
                newToken = readVariable();
                currentGroup = Token.GroupsType.VARIABLE;
            }

            else {
                switch (c) {
                    case '+': newToken = new Token(Token.Type.PLUS, "+"); currentGroup = Token.GroupsType.SIGN; break;
                    case '-': newToken = new Token(Token.Type.MINUS, "-"); currentGroup = Token.GroupsType.SIGN; break;
                    case '*': newToken = new Token(Token.Type.MULTIPLY, "*"); currentGroup = Token.GroupsType.SIGN; break;
                    case '/': newToken = new Token(Token.Type.DIVIDE, "/"); currentGroup = Token.GroupsType.SIGN; break;
                    case '(': newToken = new Token(Token.Type.LPAREN, "("); currentGroup = Token.GroupsType.PAREN; break;
                    case ')': newToken = new Token(Token.Type.RPAREN, ")"); currentGroup = Token.GroupsType.PAREN; break;
                    default:
                        throw new RuntimeException("Unexpected symbol: " + c);
                }
                pos++;
            }

            updateBalance(newToken.type);
            checkSyntax(lastGroup, lastType, currentGroup, newToken, tokens.isEmpty());

            tokens.add(newToken);
            lastGroup = currentGroup;
            lastType = newToken.type;
        }

        if (lastGroup == Token.GroupsType.SIGN) {
            throw new IllegalArgumentException("Error: expression cannot end with an operator");
        }
        if (parenthesisBalance != 0) {
            throw new IllegalArgumentException("Error: unbalanced parentheses! (Count: " + parenthesisBalance + ")");
        }

        return tokens;
    }

    private void updateBalance(Token.Type type) {
        if (type == Token.Type.LPAREN) parenthesisBalance++;
        if (type == Token.Type.RPAREN) {
            parenthesisBalance--;
            if (parenthesisBalance < 0) {
                throw new IllegalArgumentException("Error: closing parenthesis without opening one");
            }
        }
    }

    private void checkSyntax(Token.GroupsType lastGroup, Token.Type lastType,
                             Token.GroupsType currentGroup, Token currentToken, boolean isFirst) {

        if (isFirst && currentGroup == Token.GroupsType.SIGN && currentToken.type != Token.Type.MINUS) {
            throw new IllegalArgumentException("Error: expression cannot start with " + currentToken.value);
        }

        if (lastGroup != null) {
            if (lastGroup == Token.GroupsType.SIGN && currentGroup == Token.GroupsType.SIGN) {
                throw new IllegalArgumentException("Error: double operators near " + currentToken.value);
            }

            boolean isLastOperand = (lastGroup == Token.GroupsType.NUMBER || lastGroup == Token.GroupsType.VARIABLE);
            boolean isCurrentOperand = (currentGroup == Token.GroupsType.NUMBER || currentGroup == Token.GroupsType.VARIABLE);
            if (isLastOperand && isCurrentOperand) {
                throw new IllegalArgumentException("Error: missing operator between operands");
            }

            if (lastType == Token.Type.LPAREN && currentToken.type == Token.Type.RPAREN) {
                throw new IllegalArgumentException("Error: empty parentheses ()");
            }

            if (lastType == Token.Type.LPAREN && currentGroup == Token.GroupsType.SIGN && currentToken.type != Token.Type.MINUS) {
                throw new IllegalArgumentException("Error: unexpected operator after '(': " + currentToken.value);
            }

            // Ошибка: знак перед закрывающей скобкой (напр. "5+)")
            if (lastGroup == Token.GroupsType.SIGN && currentToken.type == Token.Type.RPAREN) {
                throw new IllegalArgumentException("Error: operator before ')': " + currentToken.value);
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