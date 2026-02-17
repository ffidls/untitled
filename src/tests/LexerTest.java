package tests;

import lexer.Lexer;
import lexer.Token;

import java.util.List;

public class LexerTest {

    public static void run() {
        System.out.println("\n── LexerTest ───────────────────────────────────────");

        // ── Корректный токенайзинг ───────────────────────────────────────────

        TestFramework.test("single number tokenized", new Runnable() { public void run() {
            List<Token> t = new Lexer("42").tokenize();
            TestFramework.assertEquals(1, t.size(), "size");
            TestFramework.assertEquals(Token.Type.NUMBER, t.get(0).type, "type");
            TestFramework.assertEquals("42", t.get(0).value, "value");
        }});

        TestFramework.test("single variable tokenized", new Runnable() { public void run() {
            List<Token> t = new Lexer("abc").tokenize();
            TestFramework.assertEquals(1, t.size(), "size");
            TestFramework.assertEquals(Token.Type.VARIABLE, t.get(0).type, "type");
            TestFramework.assertEquals("abc", t.get(0).value, "value");
        }});

        TestFramework.test("addition produces 3 tokens", new Runnable() { public void run() {
            List<Token> t = new Lexer("1 + 2").tokenize();
            TestFramework.assertEquals(3, t.size(), "size");
            TestFramework.assertEquals(Token.Type.PLUS, t.get(1).type, "PLUS");
        }});

        TestFramework.test("all four operators recognized", new Runnable() { public void run() {
            List<Token> t = new Lexer("a + b - c * d / e").tokenize();
            TestFramework.assertEquals(9, t.size(), "size");
            TestFramework.assertEquals(Token.Type.PLUS,     t.get(1).type, "+");
            TestFramework.assertEquals(Token.Type.MINUS,    t.get(3).type, "-");
            TestFramework.assertEquals(Token.Type.MULTIPLY, t.get(5).type, "*");
            TestFramework.assertEquals(Token.Type.DIVIDE,   t.get(7).type, "/");
        }});

        TestFramework.test("parentheses tokenized", new Runnable() { public void run() {
            List<Token> t = new Lexer("(1 + 2)").tokenize();
            TestFramework.assertEquals(5, t.size(), "size");
            TestFramework.assertEquals(Token.Type.LPAREN, t.get(0).type, "LPAREN");
            TestFramework.assertEquals(Token.Type.RPAREN, t.get(4).type, "RPAREN");
        }});

        TestFramework.test("nested parentheses tokenized", new Runnable() { public void run() {
            List<Token> t = new Lexer("((1 + 2) * 3)").tokenize();
            TestFramework.assertEquals(9, t.size(), "size");
        }});

        TestFramework.test("whitespace is ignored", new Runnable() { public void run() {
            List<Token> t1 = new Lexer("1+2").tokenize();
            List<Token> t2 = new Lexer("  1  +  2  ").tokenize();
            TestFramework.assertEquals(t1.size(), t2.size(), "size");
            for (int i = 0; i < t1.size(); i++) {
                TestFramework.assertEquals(t1.get(i).type,  t2.get(i).type,  "type[" + i + "]");
                TestFramework.assertEquals(t1.get(i).value, t2.get(i).value, "value[" + i + "]");
            }
        }});

        TestFramework.test("variable with digits (x2)", new Runnable() { public void run() {
            List<Token> t = new Lexer("x2").tokenize();
            TestFramework.assertEquals(1, t.size(), "size");
            TestFramework.assertEquals(Token.Type.VARIABLE, t.get(0).type, "type");
            TestFramework.assertEquals("x2", t.get(0).value, "value");
        }});

        TestFramework.test("multi-digit number", new Runnable() { public void run() {
            List<Token> t = new Lexer("12345").tokenize();
            TestFramework.assertEquals("12345", t.get(0).value, "value");
        }});

        TestFramework.test("unary minus at start is allowed", new Runnable() { public void run() {
            TestFramework.assertDoesNotThrow(new Runnable() { public void run() {
                new Lexer("-5").tokenize();
            }}, "unary minus");
        }});

        TestFramework.test("unary minus after LPAREN is allowed", new Runnable() { public void run() {
            TestFramework.assertDoesNotThrow(new Runnable() { public void run() {
                new Lexer("(-5 + 3)").tokenize();
            }}, "minus after (");
        }});

        TestFramework.test("complex expression has 11 tokens", new Runnable() { public void run() {
            List<Token> t = new Lexer("4 + 5 + x - (8 + x2)").tokenize();
            TestFramework.assertEquals(11, t.size(), "size");
        }});

        TestFramework.test("Token.toString() format", new Runnable() { public void run() {
            Token tok = new Token(Token.Type.PLUS, "+");
            TestFramework.assertEquals("Token[PLUS, '+']", tok.toString(), "toString");
        }});

        // ── Ошибки: недопустимый символ ─────────────────────────────────────

        TestFramework.test("symbol '@' throws RuntimeException", new Runnable() { public void run() {
            TestFramework.assertThrows(RuntimeException.class, new Runnable() { public void run() {
                new Lexer("1 @ 2").tokenize();
            }}, "@");
        }});

        TestFramework.test("dot throws RuntimeException", new Runnable() { public void run() {
            TestFramework.assertThrows(RuntimeException.class, new Runnable() { public void run() {
                new Lexer("1.5").tokenize();
            }}, "dot");
        }});

        // ── Ошибки: синтаксис ────────────────────────────────────────────────

        TestFramework.test("starts with + throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("+5").tokenize();
            }}, "+5");
        }});

        TestFramework.test("starts with * throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("*5").tokenize();
            }}, "*5");
        }});

        TestFramework.test("starts with / throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("/5").tokenize();
            }}, "/5");
        }});

        TestFramework.test("ends with operator throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("5 +").tokenize();
            }}, "5+");
        }});

        TestFramework.test("double operators throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("5 + + 3").tokenize();
            }}, "++");
        }});

        TestFramework.test("mixed double operators throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("5 * - 3").tokenize();
            }}, "*-");
        }});

        TestFramework.test("missing operator between numbers throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("5 3").tokenize();
            }}, "5 3");
        }});

        TestFramework.test("missing operator between variables throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("a b").tokenize();
            }}, "a b");
        }});

        TestFramework.test("missing operator number then variable throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("5 a").tokenize();
            }}, "5 a");
        }});

        // ── Ошибки: скобки ───────────────────────────────────────────────────

        TestFramework.test("empty parentheses throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("()").tokenize();
            }}, "()");
        }});

        TestFramework.test("unbalanced open paren throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("(1 + 2").tokenize();
            }}, "(1+2");
        }});

        TestFramework.test("unbalanced close paren throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("1 + 2)").tokenize();
            }}, "1+2)");
        }});

        TestFramework.test("closing paren without opening throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer(")5").tokenize();
            }}, ")5");
        }});

        TestFramework.test("operator after '(' throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("(+ 5)").tokenize();
            }}, "(+ 5)");
        }});

        TestFramework.test("operator before ')' throws", new Runnable() { public void run() {
            TestFramework.assertThrows(IllegalArgumentException.class, new Runnable() { public void run() {
                new Lexer("(5 +)").tokenize();
            }}, "(5 +)");
        }});
    }

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                   LexerTest                     ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        run();
        TestFramework.printSummary();
        if (TestFramework.failed > 0) System.exit(1);
    }
}
