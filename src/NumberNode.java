import java.util.Map;

public class NumberNode implements Node{
    private final int value;

    public NumberNode(int value){
        this.value = value;
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + value);
    }
}
