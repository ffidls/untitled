package tests;

import lexer.Lexer;
import lexer.Token;
import java.util.List;

public class LexerTests {

    public static void main(String[] args) {
        System.out.println("=== Starting Lexer Comprehensive Tests ===\n");

        // --- SECTION 1: POSITIVE TESTS (Valid Expressions) ---
        System.out.println("--- Positive Tests ---");
        test("Simple Addition", "12 + 45");
        test("Complex Expression", "(x + y) * (10 / 2) - 5");
        test("Nested Parentheses", "((1 + 2) * (3 - (4 / 5)))");
        test("Variables with Numbers", "var1 + temp23 * _data");
        test("Unary Minus at Start", "-100 + 50");
        test("Unary Minus in Parentheses", "5 + (-3 * 2)");
        test("Heavy Whitespace", "  x    +  (  y   * 10 )  ");
        test("Tabs and Newlines", "\t12 \n + \r 5");

        // --- SECTION 2: NEGATIVE TESTS (Expected Errors) ---
        System.out.println("\n--- Negative Tests (Error Handling) ---");

        // Parentheses Errors
        testError("Empty Parentheses", "()", "empty parentheses");
        testError("Unbalanced (Missing Close)", "((1 + 2) * 3", "unbalanced parentheses");
        testError("Unbalanced (Extra Close)", "(1 + 2))", "closing parenthesis without opening one");
        testError("Immediately Nested Empty", "(())", "empty parentheses");

        // Operator Errors
        testError("Double Operators", "10 ++ 5", "double operators");
        testError("Triple Operators", "10 + * / 5", "double operators");
        testError("Leading Multiply", "* 5 + 2", "cannot start with *");
        testError("Leading Divide", "/ 10", "cannot start with /");
        testError("Trailing Operator", "5 + 3 -", "cannot end with an operator");
        testError("Operator After Open Paren", "(* 5)", "unexpected operator after '('");
        testError("Operator Before Close Paren", "(5 + )", "operator before ')'");

        // Operand Errors
        testError("Missing Operator (Num Num)", "12 34", "missing operator between operands");
        testError("Missing Operator (Var Num)", "x 50", "missing operator between operands");
        testError("Missing Operator (Num Var)", "50 x", "missing operator between operands");
        testError("Invalid Characters", "x # y", "Unexpected symbol: #");

        // --- SECTION 3: EDGE CASES (Non-Standard Inputs) ---
        System.out.println("\n--- Edge Cases ---");
        testError("Empty String", "", "expression is empty");
        testError("Only Spaces", "     ", "expression is empty");
        testError("Null-like string", " \t \n ", "expression is empty");

        System.out.println("\n=== All Tests Completed ===");
    }

    /**
     * Helper to test valid expressions.
     * It ensures the Lexer can process the string without throwing exceptions.
     */
    private static void test(String testName, String input) {
        try {
            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.tokenize();
            System.out.printf("[PASS] %-30s | Tokens found: %d\n", testName, tokens.size());
        } catch (Exception e) {
            System.out.printf("[FAIL] %-30s | Unexpected Error: %s\n", testName, e.getMessage());
        }
    }

    /**
     * Helper to test invalid expressions.
     * It expects an IllegalArgumentException or RuntimeException with a specific message.
     */
    private static void testError(String testName, String input, String expectedPartialMessage) {
        try {
            Lexer lexer = new Lexer(input);
            lexer.tokenize();
            System.out.printf("[FAIL] %-30s | Should have thrown an error!\n", testName);
        } catch (Exception e) {
            String actualMessage = e.getMessage().toLowerCase();
            String expected = expectedPartialMessage.toLowerCase();

            if (actualMessage.contains(expected)) {
                System.out.printf("[PASS] %-30s | Caught expected error: %s\n", testName, e.getMessage());
            } else {
                System.out.printf("[FAIL] %-30s | Caught WRONG error. Expected: '%s', Got: '%s'\n",
                        testName, expected, e.getMessage());
            }
        }
    }
}