package tests;

import java.util.ArrayList;
import java.util.List;

public class TestFramework {

    public static int passed = 0;
    public static int failed = 0;
    public static final List<String> failures = new ArrayList<String>();

    public static void test(String name, Runnable body) {
        try {
            body.run();
            passed++;
            System.out.println("  [PASS] " + name);
        } catch (Throwable e) {
            failed++;
            String msg = "  [FAIL] " + name + "\n         → " + e.getMessage();
            failures.add(msg);
            System.out.println(msg);
        }
    }

    public static void assertEquals(int expected, int actual, String hint) {
        if (expected != actual) {
            throw new AssertionError("expected <" + expected + "> but was <" + actual + "> | " + hint);
        }
    }

    public static void assertEquals(Object expected, Object actual, String hint) {
        if (!expected.equals(actual)) {
            throw new AssertionError("expected <" + expected + "> but was <" + actual + "> | " + hint);
        }
    }

    public static void assertTrue(boolean condition, String hint) {
        if (!condition) throw new AssertionError("condition is false | " + hint);
    }

    public static void assertThrows(Class<? extends Throwable> type, Runnable body, String hint) {
        try {
            body.run();
            throw new AssertionError("expected " + type.getSimpleName() + " but nothing was thrown | " + hint);
        } catch (AssertionError ae) {
            throw ae;
        } catch (Throwable t) {
            if (!type.isInstance(t)) {
                throw new AssertionError("expected " + type.getSimpleName()
                        + " but got " + t.getClass().getSimpleName() + " | " + hint);
            }
        }
    }

    public static void assertDoesNotThrow(Runnable body, String hint) {
        try {
            body.run();
        } catch (Throwable t) {
            throw new AssertionError("unexpected " + t.getClass().getSimpleName()
                    + ": " + t.getMessage() + " | " + hint);
        }
    }

    public static void printSummary() {
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.printf("  Results: %d passed, %d failed, %d total%n",
                passed, failed, passed + failed);
        System.out.println("══════════════════════════════════════════════════");
        if (!failures.isEmpty()) {
            System.out.println("\nFailed tests:");
            for (String f : failures) System.out.println(f);
        } else {
            System.out.println("\n  All tests passed!");
        }
    }
}
