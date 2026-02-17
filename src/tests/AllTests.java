package tests;

public class AllTests {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║           TestTask_v2  —  All Tests              ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        LexerTest.run();
        IntegerNodeTest.run();
        VariableNodeTest.run();
        BinaryOperatorNodeTest.run();
        ParserTest.run();

        TestFramework.printSummary();
        if (TestFramework.failed > 0) System.exit(1);
    }
}
