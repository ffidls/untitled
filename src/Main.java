import tree.Node;
import tree.NumberNode;
import tree.VariableNode;

public static void main(String[] args) {

    Map<String, Integer> myVars = new HashMap<>();
    myVars.put("x", 5);

    Node num = new NumberNode(10);
    Node var = new VariableNode("x");

    // Node sum = new AddNode(num, var);

    // System.out.println("result 10 + x: " + sum.evaluate(myVars));

    //System.out.println("tree:");
    // sum.print("");
}