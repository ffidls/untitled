Project Description:
This is a Java 8 console application designed to parse mathematical expressions and build an Abstract Syntax Tree (AST). The application evaluates expressions containing 16-bit integers, variables, and basic arithmetic operators

Features:
- Expression Parsing: Supports +, -, *, / (integer division), and parentheses.
- AST Generation: Recursively builds a tree structure representing the operation hierarchy.
- Variable Management:
  - Automatically detects variables and initializes them with random values (0-65535) upon first encounter.
  - Allows manual value assignment (e.g., x = 20).
- Error Handling: Custom exception classes handle syntax errors, unbalanced parentheses, and division by zero.

Project Structure:
- lexer: Converts the input string into a stream of tokens and performs initial syntax validation.
- parser: Implements a recursive descent logic to transform tokens into an AST.
- tree: Contains the Node interface and implementations for Operators, Integers, and Variables.
- exceptions: Defines InvalidSyntaxException and EvaluationException for precise error reporting.
- Main.java: Manages the user interface, command loop, and application state.

Requirements:
- Java Development Kit (JDK) 8 or higher.
- Terminal or Command Prompt

Build and Run Instructions:
1. Navigate to the root directory containing the src folder and execute the following command:
   javac -d out src/exceptions/*.java src/lexer/*.java src/tree/*.java src/parser/*.java src/Main.java
2. Run the application using:
   java -cp out Main

Interactive Commands:
After entering an initial expression, you can use the following commands:
- print: Displays the visual structure of the AST.
- calc: Re-evaluates the expression using current variable values.
- vars: Shows all active variables and their assigned values.
- [variable] = [value]: Assigns a specific integer to a variable (e.g., a = 500).
- exit: Terminates the application.
