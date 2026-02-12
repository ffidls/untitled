import java.util.Map;

public interface Node {

    int evaluate(Map<String, Integer> variables);
    void print(String indent);

}
