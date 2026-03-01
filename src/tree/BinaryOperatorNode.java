package tree;

import java.util.Map;
import exceptions.EvaluationException;



public class BinaryOperatorNode implements Node {
    private final Node left;
    private final Node right;
    private final String operator;

    public BinaryOperatorNode(Node left, Node right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public int evaluate(Map<String, Integer> variables) throws EvaluationException {
        int l = left.evaluate(variables);
        int r = right.evaluate(variables);


        switch (operator) {
            case "+":
                return l + r;
            case "-":
                return l - r;
            case "*":
                return l * r;
            case "/":
                if (r == 0) {
                    throw new EvaluationException("Error: divide by 0");
                }
                return l / r;
            default:
                throw new EvaluationException("Error: unknown operator '" + operator + "'");
        }
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Operator: " + operator);
        left.print(indent + "  ");
        right.print(indent + "  ");
    }
}