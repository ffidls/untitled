package tests;

import lexer.Lexer;
import lexer.Token;
import exceptions.InvalidSyntaxException;
import java.util.List;

public class LexerTest {

    static int passed = 0;
    static int failed = 0;

    @FunctionalInterface
    interface LexerAction {
        void run() throws InvalidSyntaxException;
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

    static boolean throws_(LexerAction r) {
        try {
            r.run();
            return false;
        } catch (InvalidSyntaxException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            // number
            List<Token> t = new Lexer("42").tokenize();
            check("number 42 -> 1 token",       t.size() == 1);
            check("token type is NUMBER",        t.get(0).type == Token.Type.NUMBER);
            check("token value is '42'",         t.get(0).value.equals("42"));

            // variable
            t = new Lexer("abc").tokenize();
            check("variable abc -> 1 token",    t.size() == 1);
            check("token type is VARIABLE",     t.get(0).type == Token.Type.VARIABLE);

            // addition
            // "1 + 2" -> 3 токена: [1], [+], [2]
            t = new Lexer("1 + 2").tokenize();
            check("1 + 2 -> 3 tokens",          t.size() == 3);
            check("middle token is PLUS",       t.get(1).type == Token.Type.PLUS);

            // all operators
            // "a + b - c * d / e" -> 9 токенов
            t = new Lexer("a + b - c * d / e").tokenize();
            check("9 tokens total",             t.size() == 9);
            check("MINUS at index 3",           t.get(3).type == Token.Type.MINUS);
            check("MULTIPLY at index 5",        t.get(5).type == Token.Type.MULTIPLY);
            check("DIVIDE at index 7",          t.get(7).type == Token.Type.DIVIDE);

            // parentheses
            t = new Lexer("(1 + 2)").tokenize();
            check("first token is LPAREN",      t.get(0).type == Token.Type.LPAREN);
            check("last token is RPAREN",       t.get(4).type == Token.Type.RPAREN);

            // whitespace
            List<Token> t1 = new Lexer("1+2").tokenize();
            List<Token> t2 = new Lexer("  1  +  2  ").tokenize();
            check("whitespace is ignored",      t1.size() == t2.size());

            // variable with digit
            t = new Lexer("x2").tokenize();
            check("x2 type is VARIABLE",       t.get(0).type == Token.Type.VARIABLE);
            check("x2 value is 'x2'",          t.get(0).value.equals("x2"));

            // unary minus
            check("unary minus is allowed",    !throws_(() -> new Lexer("-5").tokenize()));

            // errors
            check("unknown symbol @",           throws_(() -> new Lexer("1 @ 2").tokenize()));
            check("dot is not allowed",         throws_(() -> new Lexer("1.5").tokenize()));
            check("starts with +",              throws_(() -> new Lexer("+5").tokenize()));
            check("starts with *",              throws_(() -> new Lexer("*5").tokenize()));
            check("ends with operator",         throws_(() -> new Lexer("5 +").tokenize()));
            check("double operator",            throws_(() -> new Lexer("5 + + 3").tokenize()));
            check("two numbers in a row",       throws_(() -> new Lexer("5 3").tokenize()));
            check("two variables in a row",     throws_(() -> new Lexer("a b").tokenize()));
            check("empty parentheses",          throws_(() -> new Lexer("()").tokenize()));
            check("unclosed parenthesis",       throws_(() -> new Lexer("(1 + 2").tokenize()));
            check("extra closing parenthesis",  throws_(() -> new Lexer("1 + 2)").tokenize()));
            check("operator after (",           throws_(() -> new Lexer("(+ 5)").tokenize()));
            check("operator before )",          throws_(() -> new Lexer("(5 +)").tokenize()));

        } catch (Exception e) {
            System.out.println("LEXER TEST SYSTEM ERROR: " + e.getMessage());
        }

        System.out.println("\nLexerTest Summary: " + passed + " passed, " + failed + " failed");

        if (failed > 0) System.exit(1);
    }
}