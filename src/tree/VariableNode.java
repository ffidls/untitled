package tree;

import java.util.Map;

public class VariableNode implements Node {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(Map<String, Integer> variables) {
        return variables.getOrDefault(name, 0);
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name);
    }
}