package tests;

import tree.BinaryOperatorNode;
import tree.IntegerNode;
import tree.Node;
import tree.VariableNode;
import exceptions.EvaluationException;
import java.util.HashMap;

public class BinaryOperatorNodeTest {

    static int passed = 0;
    static int failed = 0;

    @FunctionalInterface
    interface Evaluator {
        void run() throws EvaluationException;
    }

    static void check(String name, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    static boolean throws_(Evaluator r) {
        try {
            r.run();
            return false;
        } catch (EvaluationException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            HashMap<String, Integer> vars = new HashMap<String, Integer>();
            vars.put("x", 5);
            vars.put("y", 10);
            vars.put("zero", 0);

            HashMap<String, Integer> empty = new HashMap<String, Integer>();

            check("3 + 4 = 7",   new BinaryOperatorNode(new IntegerNode(3),  new IntegerNode(4),  "+").evaluate(empty) == 7);
            check("10 - 3 = 7",  new BinaryOperatorNode(new IntegerNode(10), new IntegerNode(3),  "-").evaluate(empty) == 7);
            check("6 * 7 = 42",  new BinaryOperatorNode(new IntegerNode(6),  new IntegerNode(7),  "*").evaluate(empty) == 42);
            check("20 / 4 = 5",  new BinaryOperatorNode(new IntegerNode(20), new IntegerNode(4),  "/").evaluate(empty) == 5);
            check("3 - 10 = -7", new BinaryOperatorNode(new IntegerNode(3),  new IntegerNode(10), "-").evaluate(empty) == -7);

            check("x + y = 15",  new BinaryOperatorNode(new VariableNode("x"), new VariableNode("y"), "+").evaluate(vars) == 15);
            check("x * 3 = 15",  new BinaryOperatorNode(new VariableNode("x"), new IntegerNode(3),    "*").evaluate(vars) == 15);

            final Node divByZero = new BinaryOperatorNode(new IntegerNode(10), new IntegerNode(0), "/");
            final Node divByVar  = new BinaryOperatorNode(new IntegerNode(10), new VariableNode("zero"), "/");

            check("divide by 0 throws exception", throws_(() -> divByZero.evaluate(empty)));
            check("divide by zero variable throws exception", throws_(() -> divByVar.evaluate(vars)));

            Node add = new BinaryOperatorNode(new IntegerNode(2), new IntegerNode(3), "+");
            Node mul = new BinaryOperatorNode(add, new IntegerNode(4), "*");
            check("(2 + 3) * 4 = 20", mul.evaluate(empty) == 20);

        } catch (EvaluationException e) {
            System.out.println("CRITICAL ERROR: Unexpected evaluation exception in test logic: " + e.getMessage());
        }

        System.out.println("\nBinaryOperatorNodeTest Summary: " + passed + " passed, " + failed + " failed");

        if (failed > 0) {
            System.exit(1);
        }
    }
}