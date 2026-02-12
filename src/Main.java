public static void main(String[] args) {

    Map<String, Integer> myVars = new HashMap<>();
    myVars.put("x", 5);

    Node num = new NumberNode(10);
    Node var = new VariableNode("x");

    // 2. Собираем их в Плюс
    Node sum = new AddNode(num, var);

    // 3. Считаем всё дерево сразу
    System.out.println("Результат 10 + x: " + sum.evaluate(myVars)); // Будет 15

    System.out.println("Вид дерева:");
    sum.print("");
}