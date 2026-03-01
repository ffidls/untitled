package tree;

import java.util.Map;
import exceptions.EvaluationException;

public interface Node {

    int evaluate(Map<String, Integer> variables) throws EvaluationException;
    void print(String indent);

}
