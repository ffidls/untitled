import java.util.Map;

public class BinaryOperatorNode implements Node {
    private final Node left;
    private final Node right;
    private final char operator;

    public BinaryOperatorNode(Node left, Node right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        int l = left.evaluate(variables);
        int r = right.evaluate(variables);

        return switch (operator) {
            case '+' -> l + r;
            case '-' -> l - r;
            case '*' -> l * r;
            case '/' -> {
                if (r == 0) throw new ArithmeticException("Error: divide by 0");
                yield l / r;
            }
            default -> 0;
        };
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + operator);
        left.print(indent + "  ");
        right.print(indent + "  ");
    }
}