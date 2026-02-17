package tree;

import java.util.Map;

public class IntegerNode implements Node{
    private final int value;

    public IntegerNode(int value){
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
