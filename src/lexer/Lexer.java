package lexer;

import exceptions.InvalidSyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;
    private int parenthesisBalance = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() throws InvalidSyntaxException {
        List<Token> tokens = new ArrayList<>();
        Token.GroupsType lastGroup = null;
        Token.Type lastType = null;

        if (input.trim().isEmpty()) {
            throw new InvalidSyntaxException("Error: expression is empty.");
        }

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
            } else if (Character.isLetter(c)) {
                newToken = readVariable();
                currentGroup = Token.GroupsType.VARIABLE;
            } else {
                switch (c) {
                    case '+': newToken = new Token(Token.Type.PLUS, "+"); currentGroup = Token.GroupsType.SIGN; break;
                    case '-': newToken = new Token(Token.Type.MINUS, "-"); currentGroup = Token.GroupsType.SIGN; break;
                    case '*': newToken = new Token(Token.Type.MULTIPLY, "*"); currentGroup = Token.GroupsType.SIGN; break;
                    case '/': newToken = new Token(Token.Type.DIVIDE, "/"); currentGroup = Token.GroupsType.SIGN; break;
                    case '(': newToken = new Token(Token.Type.LPAREN, "("); currentGroup = Token.GroupsType.PAREN; break;
                    case ')': newToken = new Token(Token.Type.RPAREN, ")"); currentGroup = Token.GroupsType.PAREN; break;
                    default:
                        throw new InvalidSyntaxException("Unexpected symbol: " + c);
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
            throw new InvalidSyntaxException("Error: expression cannot end with an operator");
        }
        if (parenthesisBalance != 0) {
            throw new InvalidSyntaxException("Error: unbalanced parentheses! (Remaining: " + parenthesisBalance + ")");
        }

        return tokens;
    }

    private void updateBalance(Token.Type type) throws InvalidSyntaxException {
        if (type == Token.Type.LPAREN) parenthesisBalance++;
        if (type == Token.Type.RPAREN) {
            parenthesisBalance--;
            if (parenthesisBalance < 0) {
                throw new InvalidSyntaxException("Error: closing parenthesis without opening one");
            }
        }
    }

    private void checkSyntax(Token.GroupsType lastGroup, Token.Type lastType,
                             Token.GroupsType currentGroup, Token currentToken, boolean isFirst) throws InvalidSyntaxException {

        if (isFirst && currentGroup == Token.GroupsType.SIGN && currentToken.type != Token.Type.MINUS) {
            throw new InvalidSyntaxException("Error: expression cannot start with " + currentToken.value);
        }

        if (lastGroup != null) {
            if (lastGroup == Token.GroupsType.SIGN && currentGroup == Token.GroupsType.SIGN) {
                throw new InvalidSyntaxException("Error: double operators near " + currentToken.value);
            }

            boolean isLastOperand = (lastGroup == Token.GroupsType.NUMBER || lastGroup == Token.GroupsType.VARIABLE);
            boolean isCurrentOperand = (currentGroup == Token.GroupsType.NUMBER || currentGroup == Token.GroupsType.VARIABLE);
            if (isLastOperand && isCurrentOperand) {
                throw new InvalidSyntaxException("Error: missing operator between operands at " + currentToken.value);
            }

            if (lastType == Token.Type.LPAREN && currentToken.type == Token.Type.RPAREN) {
                throw new InvalidSyntaxException("Error: empty parentheses ()");
            }

            if (lastType == Token.Type.LPAREN && currentGroup == Token.GroupsType.SIGN && currentToken.type != Token.Type.MINUS) {
                throw new InvalidSyntaxException("Error: unexpected operator after '(': " + currentToken.value);
            }

            if (lastGroup == Token.GroupsType.SIGN && currentToken.type == Token.Type.RPAREN) {
                throw new InvalidSyntaxException("Error: operator before ')': " + currentToken.value);
            }
        }
    }

    private Token readNumber() throws InvalidSyntaxException {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        String val = sb.toString();

        try {
            long num = Long.parseLong(val);
            if (num > 65535) {
                throw new InvalidSyntaxException("Error: Number " + val + " exceeds 16-bit limit (65535)");
            }
        } catch (NumberFormatException e) {
            throw new InvalidSyntaxException("Error: Invalid number format: " + val);
        }

        return new Token(Token.Type.NUMBER, val);
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