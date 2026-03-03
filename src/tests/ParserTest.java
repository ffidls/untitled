package tests;

import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import tree.Node;
import exceptions.InvalidSyntaxException;
import exceptions.EvaluationException;
import exceptions.MathAppException;
import java.util.HashMap;
import java.util.List;

public class ParserTest {

    static int passed = 0;
    static int failed = 0;

    @FunctionalInterface
    interface TestAction {
        void run() throws MathAppException;
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

    static boolean throws_(TestAction r) {
        try {
            r.run();
            return false;
        } catch (MathAppException e) {
            return true;
        }
    }

    static Node parse(String expr) throws InvalidSyntaxException {
        Lexer lexer = new Lexer(expr);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        return parser.parse();
    }

    static int eval(String expr, HashMap<String, Integer> vars) throws MathAppException {
        return parse(expr).evaluate(vars);
    }

    public static void main(String[] args) {
        try {
            HashMap<String, Integer> empty = new HashMap<String, Integer>();
            HashMap<String, Integer> vars = new HashMap<String, Integer>();
            vars.put("x", 5);
            vars.put("y", 10);
            vars.put("zero", 0);

            check("number 7",               eval("7", empty) == 7);
            check("variable x = 5",         eval("x", vars)  == 5);

            check("unknown variable throws exception", throws_(() -> eval("z", empty)));

            check("2 + 3 = 5",              eval("2 + 3", empty) == 5);
            check("4 - 3 = 1",              eval("4 - 3", empty) == 1);
            check("3 * 4 = 12",             eval("3 * 4", empty) == 12);
            check("9 / 3 = 3",              eval("9 / 3", empty) == 3);

            // operator precedence
            check("2 + 3 * 4 = 14",         eval("2 + 3 * 4",  empty) == 14);
            check("10 - 6 / 2 = 7",         eval("10 - 6 / 2", empty) == 7);
            check("8 - 3 - 2 = 3",          eval("8 - 3 - 2",  empty) == 3);

            // parentheses
            check("(2 + 3) * 4 = 20",       eval("(2 + 3) * 4",       empty) == 20);
            check("10 - (3 + 2) = 5",       eval("10 - (3 + 2)",      empty) == 5);
            check("(2 + 3) * (4 - 1) = 15", eval("(2 + 3) * (4 - 1)", empty) == 15);

            // with variables
            check("x + 3 = 8",              eval("x + 3",       vars) == 8);
            check("x + y = 15",             eval("x + y",        vars) == 15);
            check("(x + 2) * 3 = 21",       eval("(x + 2) * 3", vars) == 21);

            // complex expression
            HashMap<String, Integer> v2 = new HashMap<String, Integer>();
            v2.put("x", 5); v2.put("x2", 2);
            check("4 + 5 + x - (8 + x2) = 4", eval("4 + 5 + x - (8 + x2)", v2) == 4);

            // division by zero
            check("5 / 0 throws exception",  throws_(() -> eval("5 / 0", empty)));

            // syntax errors
            check("invalid syntax ++ throws", throws_(() -> eval("5 ++ 2", empty)));
            check("empty parentheses throws", throws_(() -> eval("()", empty)));

        } catch (Exception e) {
            System.out.println("TEST SUITE FAILED: " + e.getMessage());
        }

        System.out.println("\nParserTest Summary: " + passed + " passed, " + failed + " failed");

        if (failed > 0) System.exit(1);
    }
}