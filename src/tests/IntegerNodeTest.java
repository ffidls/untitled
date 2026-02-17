package tests;

import tree.IntegerNode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class IntegerNodeTest {

    public static void run() {
        System.out.println("\n── IntegerNodeTest ─────────────────────────────────");

        TestFramework.test("positive integer evaluates correctly", new Runnable() { public void run() {
            TestFramework.assertEquals(42, new IntegerNode(42).evaluate(new HashMap<String, Integer>()), "42");
        }});

        TestFramework.test("zero evaluates to 0", new Runnable() { public void run() {
            TestFramework.assertEquals(0, new IntegerNode(0).evaluate(new HashMap<String, Integer>()), "0");
        }});

        TestFramework.test("negative integer evaluates correctly", new Runnable() { public void run() {
            TestFramework.assertEquals(-7, new IntegerNode(-7).evaluate(new HashMap<String, Integer>()), "-7");
        }});

        TestFramework.test("integer ignores variables map", new Runnable() { public void run() {
            HashMap<String, Integer> vars = new HashMap<String, Integer>();
            vars.put("x", 999);
            TestFramework.assertEquals(99, new IntegerNode(99).evaluate(vars), "ignores vars");
        }});

        TestFramework.test("large integer evaluates correctly", new Runnable() { public void run() {
            TestFramework.assertEquals(1000000, new IntegerNode(1000000).evaluate(new HashMap<String, Integer>()), "large");
        }});

        TestFramework.test("IntegerNode print outputs the value", new Runnable() { public void run() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(buf));
            new IntegerNode(5).print("  ");
            System.setOut(old);
            TestFramework.assertTrue(buf.toString().contains("5"), "contains 5");
        }});

        TestFramework.test("IntegerNode print uses provided indent", new Runnable() { public void run() {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(buf));
            new IntegerNode(3).print(">>>");
            System.setOut(old);
            TestFramework.assertTrue(buf.toString().contains(">>>"), "contains indent");
        }});
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                IntegerNodeTest                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        run();
        TestFramework.printSummary();
        if (TestFramework.failed > 0) System.exit(1);
    }
}
