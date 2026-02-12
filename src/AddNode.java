import java.util.Map;

public class AddNode implements Node {
    private final Node left;
    private final Node right;

    public AddNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return left.evaluate(variables) + right.evaluate(variables);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "+");
        left.print(indent + "  ");
        right.print(indent + "  ");
    }
}