package tests;

import tree.BinaryOperatorNode;
import tree.IntegerNode;
import tree.Node;
import tree.VariableNode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class BinaryOperatorNodeTest {

    private static final Map<String, Integer> VARS = new HashMap<String, Integer>();
    static {
        VARS.put("x",    5);
        VARS.put("y",   10);
        VARS.put("zero", 0);
    }

    public static void run() {
        System.out.println("\n── BinaryOperatorNodeTest ──────────────────────────");

        // ── Базовые операции ─────────────────────────────────────────────────

        TestFramework.test("addition 3+4=7", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(3), new IntegerNode(4), "+");
            TestFramework.assertEquals(7, n.evaluate(new HashMap<String, Integer>()), "+");
        }});

        TestFramework.test("subtraction 10-3=7", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(10), new IntegerNode(3), "-");
            TestFramework.assertEquals(7, n.evaluate(new HashMap<String, Integer>()), "-");
        }});

        TestFramework.test("multiplication 6*7=42", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(6), new IntegerNode(7), "*");
            TestFramework.assertEquals(42, n.evaluate(new HashMap<String, Integer>()), "*");
        }});

        TestFramework.test("division 20/4=5", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(20), new IntegerNode(4), "/");
            TestFramework.assertEquals(5, n.evaluate(new HashMap<String, Integer>()), "/");
        }});

        TestFramework.test("negative result 3-10=-7", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(3), new IntegerNode(10), "-");
            TestFramework.assertEquals(-7, n.evaluate(new HashMap<String, Integer>()), "3-10");
        }});

        TestFramework.test("addition with zero 5+0=5", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(5), new IntegerNode(0), "+");
            TestFramework.assertEquals(5, n.evaluate(new HashMap<String, Integer>()), "5+0");
        }});

        // ── Ошибки ───────────────────────────────────────────────────────────

        TestFramework.test("division by zero literal throws ArithmeticException", new Runnable() { public void run() {
            final Node n = new BinaryOperatorNode(new IntegerNode(10), new IntegerNode(0), "/");
            TestFramework.assertThrows(ArithmeticException.class, new Runnable() { public void run() {
                n.evaluate(new HashMap<String, Integer>());
            }}, "/0");
        }});

        TestFramework.test("division by zero variable throws ArithmeticException", new Runnable() { public void run() {
            final Node n = new BinaryOperatorNode(new IntegerNode(10), new VariableNode("zero"), "/");
            TestFramework.assertThrows(ArithmeticException.class, new Runnable() { public void run() {
                n.evaluate(VARS);
            }}, "/zero");
        }});

        TestFramework.test("unknown operator returns 0", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new IntegerNode(5), new IntegerNode(3), "^");
            TestFramework.assertEquals(0, n.evaluate(new HashMap<String, Integer>()), "^");
        }});

        // ── С переменными ────────────────────────────────────────────────────

        TestFramework.test("addition with variables x+y=15", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new VariableNode("x"), new VariableNode("y"), "+");
            TestFramework.assertEquals(15, n.evaluate(VARS), "x+y");
        }});

        TestFramework.test("multiplication with variable x*3=15", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new VariableNode("x"), new IntegerNode(3), "*");
            TestFramework.assertEquals(15, n.evaluate(VARS), "x*3");
        }});

        TestFramework.test("subtraction variable from literal y-3=7", new Runnable() { public void run() {
            Node n = new BinaryOperatorNode(new VariableNode("y"), new IntegerNode(3), "-");
            TestFramework.assertEquals(7, n.evaluate(VARS), "y-3");
        }});

        // ── Вложенные деревья ────────────────────────────────────────────────

        TestFramework.test("nested (2+3)*4=20", new Runnable() { public void run() {
            Node add = new BinaryOperatorNode(new IntegerNode(2), new IntegerNode(3), "+");
            Node mul = new BinaryOperatorNode(add, new IntegerNode(4), "*");
            TestFramework.assertEquals(20, mul.evaluate(new HashMap<String, Integer>()), "(2+3)*4");
        }});

        TestFramework.test("deep nested ((1+2)*(3-1))/3=2", new Runnable() { public void run() {
            Node add = new BinaryOperatorNode(new IntegerNode(1), new IntegerNode(2), "+");
            Node sub = new BinaryOperatorNode(new IntegerNode(3), new IntegerNode(1), "-");
            Node mul = new BinaryOperatorNode(add, sub, "*");
            Node div = new BinaryOperatorNode(mul, new IntegerNode(3), "/");
            TestFramework.assertEquals(2, div.evaluate(new HashMap<String, Integer>()), "deep");
        }});

        TestFramework.test("chain addition (1+2)+3=6", new Runnable() { public void run() {
            Node left = new BinaryOperatorNode(new IntegerNode(1), new IntegerNode(2), "+");
            Node root = new BinaryOperatorNode(left, new IntegerNode(3), "+");
            TestFramework.assertEquals(6, root.evaluate(new HashMap<String, Integer>()), "chain+");
        }});

        // ── print ─────────────────────────────────────────────────────────────

        TestFramework.test("print contains operator symbol", new Runnable() { public void run() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(buf));
            new BinaryOperatorNode(new IntegerNode(1), new IntegerNode(2), "+").print("");
            System.setOut(old);
            TestFramework.assertTrue(buf.toString().contains("+"), "has +");
        }});

        TestFramework.test("print contains both operands", new Runnable() { public void run() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(buf));
            new BinaryOperatorNode(new IntegerNode(7), new IntegerNode(8), "*").print("");
            System.setOut(old);
            String out = buf.toString();
            TestFramework.assertTrue(out.contains("7") && out.contains("8"), "has operands");
        }});
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║            BinaryOperatorNodeTest               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        run();
        TestFramework.printSummary();
        if (TestFramework.failed > 0) System.exit(1);
    }
}
