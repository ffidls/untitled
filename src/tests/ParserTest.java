package tests;

import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import tree.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserTest {

    private static final Map<String, Integer> VARS = new HashMap<String, Integer>();
    static {
        VARS.put("x",    5);
        VARS.put("y",   10);
        VARS.put("zero", 0);
    }

    private static Node parse(String expr) {
        List<Token> tokens = new Lexer(expr).tokenize();
        return new Parser(tokens).parse();
    }

    private static int eval(String expr, Map<String, Integer> vars) {
        return parse(expr).evaluate(vars);
    }

    private static int eval(String expr) {
        return eval(expr, new HashMap<String, Integer>());
    }

    public static void run() {

        // ── Литералы ─────────────────────────────────────────────────────────

        System.out.println("\n── ParserTest: literals ────────────────────────────");

        TestFramework.test("single number 7", new Runnable() { public void run() {
            TestFramework.assertEquals(7, eval("7"), "7");
        }});

        TestFramework.test("single variable x=5", new Runnable() { public void run() {
            TestFramework.assertEquals(5, eval("x", VARS), "x");
        }});

        TestFramework.test("unknown variable evaluates to 0", new Runnable() { public void run() {
            TestFramework.assertEquals(0, eval("z"), "z");
        }});

        // ── Базовая арифметика ────────────────────────────────────────────────

        System.out.println("\n── ParserTest: basic arithmetic ────────────────────");

        TestFramework.test("2+3=5", new Runnable() { public void run() {
            TestFramework.assertEquals(5, eval("2 + 3"), "+");
        }});

        TestFramework.test("4-3=1", new Runnable() { public void run() {
            TestFramework.assertEquals(1, eval("4 - 3"), "-");
        }});

        TestFramework.test("3*4=12", new Runnable() { public void run() {
            TestFramework.assertEquals(12, eval("3 * 4"), "*");
        }});

        TestFramework.test("9/3=3", new Runnable() { public void run() {
            TestFramework.assertEquals(3, eval("9 / 3"), "/");
        }});

        // ── Приоритет операций ────────────────────────────────────────────────

        System.out.println("\n── ParserTest: operator precedence ─────────────────");

        TestFramework.test("2+3*4=14 (mul before add)", new Runnable() { public void run() {
            TestFramework.assertEquals(14, eval("2 + 3 * 4"), "prec");
        }});

        TestFramework.test("10-6/2=7 (div before sub)", new Runnable() { public void run() {
            TestFramework.assertEquals(7, eval("10 - 6 / 2"), "div/sub");
        }});

        TestFramework.test("2*3/1=6", new Runnable() { public void run() {
            TestFramework.assertEquals(6, eval("2 * 3 / 1"), "mul/div");
        }});

        TestFramework.test("5+3-2=6", new Runnable() { public void run() {
            TestFramework.assertEquals(6, eval("5 + 3 - 2"), "add/sub");
        }});

        TestFramework.test("8-3-2=3 (left associativity)", new Runnable() { public void run() {
            TestFramework.assertEquals(3, eval("8 - 3 - 2"), "left assoc");
        }});

        // ── Скобки ────────────────────────────────────────────────────────────

        System.out.println("\n── ParserTest: parentheses ─────────────────────────");

        TestFramework.test("(2+3)*4=20", new Runnable() { public void run() {
            TestFramework.assertEquals(20, eval("(2 + 3) * 4"), "(2+3)*4");
        }});

        TestFramework.test("((2+3))=5", new Runnable() { public void run() {
            TestFramework.assertEquals(5, eval("((2 + 3))"), "((2+3))");
        }});

        TestFramework.test("10-(3+2)=5", new Runnable() { public void run() {
            TestFramework.assertEquals(5, eval("10 - (3 + 2)"), "10-(3+2)");
        }});

        TestFramework.test("(2+3)*(4-1)=15", new Runnable() { public void run() {
            TestFramework.assertEquals(15, eval("(2 + 3) * (4 - 1)"), "(2+3)*(4-1)");
        }});

        // ── Переменные ────────────────────────────────────────────────────────

        System.out.println("\n── ParserTest: variables ───────────────────────────");

        TestFramework.test("x+3=8 where x=5", new Runnable() { public void run() {
            Map<String, Integer> v = new HashMap<String, Integer>();
            v.put("x", 5);
            TestFramework.assertEquals(8, eval("x + 3", v), "x+3");
        }});

        TestFramework.test("x+y=15", new Runnable() { public void run() {
            TestFramework.assertEquals(15, eval("x + y", VARS), "x+y");
        }});

        TestFramework.test("(x+2)*3=18 where x=4", new Runnable() { public void run() {
            Map<String, Integer> v = new HashMap<String, Integer>();
            v.put("x", 4);
            TestFramework.assertEquals(18, eval("(x + 2) * 3", v), "(x+2)*3");
        }});

        TestFramework.test("x2+1=8 where x2=7", new Runnable() { public void run() {
            Map<String, Integer> v = new HashMap<String, Integer>();
            v.put("x2", 7);
            TestFramework.assertEquals(8, eval("x2 + 1", v), "x2+1");
        }});

        // ── Сложные / интеграционные ──────────────────────────────────────────

        System.out.println("\n── ParserTest: complex / integration ───────────────");

        TestFramework.test("4+5+x-(8+x2)=4", new Runnable() { public void run() {
            Map<String, Integer> v = new HashMap<String, Integer>();
            v.put("x",  5);
            v.put("x2", 2);
            TestFramework.assertEquals(4, eval("4 + 5 + x - (8 + x2)", v), "complex");
        }});

        TestFramework.test("1+2*3-4/2=5", new Runnable() { public void run() {
            TestFramework.assertEquals(5, eval("1 + 2 * 3 - 4 / 2"), "mixed");
        }});

        TestFramework.test("((1+2)*(3-1))/3=2", new Runnable() { public void run() {
            TestFramework.assertEquals(2, eval("((1 + 2) * (3 - 1)) / 3"), "deep paren");
        }});

        TestFramework.test("10+2*3-8/4=14", new Runnable() { public void run() {
            TestFramework.assertEquals(14, eval("10 + 2 * 3 - 8 / 4"), "all ops");
        }});

        // ── Деление на ноль ───────────────────────────────────────────────────

        System.out.println("\n── ParserTest: division by zero ────────────────────");

        TestFramework.test("5/0 throws ArithmeticException", new Runnable() { public void run() {
            final Node n = parse("5 / 0");
            TestFramework.assertThrows(ArithmeticException.class, new Runnable() { public void run() {
                n.evaluate(new HashMap<String, Integer>());
            }}, "5/0");
        }});

        TestFramework.test("5/z throws ArithmeticException (z missing -> 0)", new Runnable() { public void run() {
            final Node n = parse("5 / z");
            TestFramework.assertThrows(ArithmeticException.class, new Runnable() { public void run() {
                n.evaluate(new HashMap<String, Integer>());
            }}, "5/z");
        }});

        // ── Список токенов хранится в Parser ─────────────────────────────────

        System.out.println("\n── ParserTest: token list ───────────────────────────");

        TestFramework.test("parser stores token list correctly", new Runnable() { public void run() {
            List<Token> tokens = new Lexer("1 + 2").tokenize();
            Parser parser = new Parser(tokens);
            TestFramework.assertEquals(tokens.size(), parser.expressionTokens.size(), "size");
            TestFramework.assertEquals(tokens.get(0).value, parser.expressionTokens.get(0).value, "first value");
        }});
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                  ParserTest                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        run();
        TestFramework.printSummary();
        if (TestFramework.failed > 0) System.exit(1);
    }
}
