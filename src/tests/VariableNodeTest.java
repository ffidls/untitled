package tests;

import tree.VariableNode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class VariableNodeTest {

    private static final Map<String, Integer> VARS = new HashMap<String, Integer>();
    static {
        VARS.put("x",    5);
        VARS.put("y",   10);
        VARS.put("zero", 0);
    }

    public static void run() {
        System.out.println("\n── VariableNodeTest ────────────────────────────────");

        TestFramework.test("known variable x returns 5", new Runnable() { public void run() {
            TestFramework.assertEquals(5, new VariableNode("x").evaluate(VARS), "x=5");
        }});

        TestFramework.test("known variable y returns 10", new Runnable() { public void run() {
            TestFramework.assertEquals(10, new VariableNode("y").evaluate(VARS), "y=10");
        }});

        TestFramework.test("unknown variable returns 0", new Runnable() { public void run() {
            TestFramework.assertEquals(0, new VariableNode("unknown").evaluate(VARS), "unknown");
        }});

        TestFramework.test("variable with value zero returns 0", new Runnable() { public void run() {
            TestFramework.assertEquals(0, new VariableNode("zero").evaluate(VARS), "zero=0");
        }});

        TestFramework.test("variable in empty map returns 0", new Runnable() { public void run() {
            TestFramework.assertEquals(0, new VariableNode("x").evaluate(new HashMap<String, Integer>()), "empty map");
        }});

        TestFramework.test("variable updated in map reflects new value", new Runnable() { public void run() {
            Map<String, Integer> vars = new HashMap<String, Integer>();
            vars.put("a", 42);
            TestFramework.assertEquals(42, new VariableNode("a").evaluate(vars), "a=42");
        }});

        TestFramework.test("VariableNode print outputs variable name", new Runnable() { public void run() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(buf));
            new VariableNode("myVar").print("--");
            System.setOut(old);
            TestFramework.assertTrue(buf.toString().contains("myVar"), "contains myVar");
        }});

        TestFramework.test("VariableNode print uses provided indent", new Runnable() { public void run() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(buf));
            new VariableNode("z").print("***");
            System.setOut(old);
            TestFramework.assertTrue(buf.toString().contains("***"), "contains indent");
        }});
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║               VariableNodeTest                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        run();
        TestFramework.printSummary();
        if (TestFramework.failed > 0) System.exit(1);
    }
}
