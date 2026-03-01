import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import tree.Node;
import exceptions.*;

import java.util.*;



public class Main {
    private static Node root = null;
    private static final Map<String, Integer> variables = new HashMap<>();
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter expression (example: (12 + x) * 23 + y):");

        String expression = "";
        while (expression.isEmpty()) {
            System.out.print("> ");
            expression = scanner.nextLine().trim();
        }

        try {
            initExpression(expression);
            System.out.println("\nRandom init + first calculation:");
            printCurrentState();
        } catch (MathAppException e) {
            System.out.println("INITIALIZATION ERROR: " + e.getMessage());
        }

        System.out.println("\nCommands:");
        System.out.println("  print          - print AST");
        System.out.println("  calc           - calculate expression");
        System.out.println("  vars           - show variables");
        System.out.println("  x = 20         - set variable value");
        System.out.println("  exit           - quit");


        while (true) {
            System.out.print("\n> ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            if (line.isEmpty()) continue;

            try {
                processCommand(line);
            } catch (InvalidSyntaxException e) {
                System.out.println("SYNTAX ERROR: " + e.getMessage());
            } catch (EvaluationException e) {
                System.out.println("CALCULATION ERROR: " + e.getMessage());
            } catch (MathAppException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("UNEXPECTED ERROR: " + e.getMessage());
            }
        }
    }

    private static void processCommand(String line) throws MathAppException {
        if (line.equalsIgnoreCase("print")) {
            if (root == null) throw new EvaluationException("No tree built. Enter a valid expression.");
            System.out.println("Abstract Syntax Tree:");
            root.print("");
        }
        else if (line.equalsIgnoreCase("calc")) {
            printCurrentState();
        }
        else if (line.equalsIgnoreCase("vars")) {
            System.out.println("Current variables: " + variables);
        }
        else if (line.contains("=")) {
            handleAssignment(line);
        }
        else {
            initExpression(line);
            printCurrentState();
        }
    }

    private static void initExpression(String expression) throws InvalidSyntaxException {
        Lexer lexer = new Lexer(expression);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);

        root = parser.parse();

        for (Token t : tokens) {
            if (t.type == Token.Type.VARIABLE && !variables.containsKey(t.value)) {
                variables.put(t.value, random.nextInt(65536));
            }
        }
    }

    private static void handleAssignment(String line) throws EvaluationException {
        String[] parts = line.split("=");
        if (parts.length != 2) throw new EvaluationException("Invalid format. Use: x = 20");

        String name = parts[0].trim();
        String valueStr = parts[1].trim();

        if (!name.matches("[A-Za-z][A-Za-z0-9]*")) {
            throw new EvaluationException("Invalid variable name: " + name);
        }

        try {
            int value = Integer.parseInt(valueStr);
            variables.put(name, value);
            System.out.println(name + " = " + value);
        } catch (NumberFormatException e) {
            throw new EvaluationException("Invalid integer value: " + valueStr);
        }
    }

    private static void printCurrentState() throws EvaluationException {
        if (root == null) throw new EvaluationException("No expression loaded.");

        System.out.println("Variables: " + variables);
        int result = root.evaluate(variables);
        System.out.println("Result: " + result);
    }
}