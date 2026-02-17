import lexer.Token;
import tree.Node;
import tree.IntegerNode;
import tree.VariableNode;

import lexer.Lexer;

public static void main(String[] args) {

    Map<String, Integer> myVars = new HashMap<>();
    myVars.put("x", 5);

    Node num = new IntegerNode(10);
    Node var = new VariableNode("x");

    Lexer lexer = new Lexer("4 + 5 + x - (8 + x2)");
    List<Token> tokens = lexer.tokenize();

    String s = "d";
    System.out.println(tokens.get(3).type);

    // Node sum = new AddNode(num, var);
    // System.out.println("result 10 + x: " + sum.evaluate(myVars));

    //System.out.println("tree:");
    // sum.print("");
}