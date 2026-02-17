package tests;

import lexer.Lexer;
import parser.Parser;
import tree.Node;
import java.util.HashMap;
import java.util.Map;

public class ParserTests {

    public static void main(String[] args) {
        System.out.println("=== Starting ULTIMATE Parser & Tree Tests ===\n");

        Map<String, Integer> context = new HashMap<>();
        context.put("x", 10);
        context.put("y", 2);
        context.put("z", 8);

        test("Double Groups", "100 - (20 + 30) - (10 - 5)", context, 45);
        test("Deep Hierarchy", "2 * (3 + 4 * (5 - 2))", context, 30);
        test("Mul/Div Associativity", "10 / 2 * 5", context, 25);
        test("Add/Sub Associativity", "10 - 2 + 5", context, 13);
        test("Variables in Action", "(x * y) / (z - 4)", context, 5);
        test("Repeated Variables", "x + x + x + x", context, 40);
        test("Insane Nesting", "((((x)))) + ((((y))))", context, 12);

        testError("Division by Zero", "10 / 0", context);
        testError("Undefined Variable", "x + unknown_var", context);

        System.out.println("\n=== All Advanced Tests Completed ===");
    }

    private static void test(String name, String input, Map<String, Integer> vars, int expected) {
        try {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer.tokenize());
            Node root = parser.parse();
            int result = root.evaluate(vars);

            if (result == expected) {
                System.out.printf("[OK]   %-25s | Result: %d\n", name, result);
            } else {
                System.out.printf("[FAIL] %-25s | Expected %d, Got %d\n", name, expected, result);
            }
        } catch (Exception e) {
            System.out.printf("[ERR]  %-25s | Unexpected Exception: %s\n", name, e.getMessage());
        }
    }

    private static void testError(String name, String input, Map<String, Integer> vars) {
        try {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer.tokenize());
            Node root = parser.parse();
            root.evaluate(vars);
            System.out.printf("[FAIL] %-25s | Should have thrown error for: %s\n", name, input);
        } catch (Exception e) {
            System.out.printf("[OK]   %-25s | Caught expected error: %s\n", name, e.getMessage());
        }
    }
}