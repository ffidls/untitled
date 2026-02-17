package parser;

import lexer.Token;
import tree.BinaryOperatorNode;
import tree.IntegerNode;
import tree.Node;
import tree.VariableNode;
import java.util.List;

public class Parser {
    public List<Token> expressionTokens;
    private int leftIndex;
    private int rightIndex;

    public Parser(List<Token> tokens){
        expressionTokens = tokens;
        this.leftIndex = 0;
        this.rightIndex = expressionTokens.size();
    }

    public Node parse() {
        return parseExpression(0, expressionTokens.size() - 1);
    }

    private Node parseExpression(int leftIndex, int rightIndex){
        int operationIndex = -1;
        String operation = "";
        int depth = 0;

        for (int index = rightIndex; index >= leftIndex; index--){
            Token token = expressionTokens.get(index);

            if (token.type.equals(Token.Type.RPAREN)) depth++;
            else if (token.type.equals(Token.Type.LPAREN)) depth--;

            if (depth == 0 && (token.type.equals(Token.Type.PLUS) || token.type.equals(Token.Type.MINUS))) {
                operationIndex = index;
                operation = token.value;
                break;
            }
        }

        if (operationIndex == -1) {
            return parseTerm(leftIndex, rightIndex);
        }

        return new BinaryOperatorNode(parseExpression(leftIndex, operationIndex - 1),
                parseTerm(operationIndex + 1, rightIndex),
                operation);
    }

    private Node parseTerm(int leftIndex, int rightIndex){
        int operationIndex = -1;
        String operation = "";
        int depth = 0;

        for (int index = rightIndex; index >= leftIndex; index--){
            Token token = expressionTokens.get(index);

            if (token.type.equals(Token.Type.RPAREN)) depth++;
            else if (token.type.equals(Token.Type.LPAREN)) depth--;

            if (depth == 0 && (token.type.equals(Token.Type.MULTIPLY) || token.type.equals(Token.Type.DIVIDE))) {
                operationIndex = index;
                operation = token.value;
                break;
            }
        }

        if (operationIndex == -1) {
            return parseFactor(leftIndex, rightIndex);
        }

        return new BinaryOperatorNode(parseTerm(leftIndex, operationIndex - 1),
                parseFactor(operationIndex + 1, rightIndex),
                operation);
    }

    private Node parseFactor(int leftIndex, int rightIndex){
        Token firstToken = expressionTokens.get(leftIndex);

        if (firstToken.type.equals(Token.Type.LPAREN)) {
            return parseExpression(leftIndex + 1, rightIndex - 1);
        }

        if (leftIndex == rightIndex) {
            Token value = expressionTokens.get(leftIndex);
            if (value.type.equals(Token.Type.VARIABLE)) {
                return new VariableNode(value.value);
            } else {
                return new IntegerNode(Integer.parseInt(value.value));
            }
        }

        throw new RuntimeException("Unexpected parsing state at index: " + leftIndex);
    }
}