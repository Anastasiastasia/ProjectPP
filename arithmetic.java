import java.util.Stack;

public class arithmetic {
    public static String processLine(String line) {
        line = line.replaceAll("\\s+", " ");
        String[] tokens = line.split(" ");
        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            if (isArithmeticExpression(token)) {
                int evaluatedResult = evaluateExpression(token);
                result.append(evaluatedResult).append(" ");
            } else {
                result.append(token).append(" ");
            }
        }
        return result.toString().trim();
    }
    private static boolean isArithmeticExpression(String token) {
        return token.matches("\\d+([+*/-]\\d+)*") || token.contains("(");
    }
    private static int evaluateExpression(String expression) {
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char token = expression.charAt(i);
            if (Character.isDigit(token)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                values.push(Integer.parseInt(sb.toString()));
                i--; 
            } else if (token == '(') {
                operators.push(token);
            } else if (token == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); 
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(token);
            }
        }
        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }
        return values.pop();
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }
    private static int applyOperation(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
    public static String processLine1(String line) {
        StringBuilder cleanedLine = new StringBuilder();
        boolean lastWasSpace = true;
        for (char c : line.toCharArray()) {
            if (c != ' ' || lastWasSpace) {
                cleanedLine.append(c);
                lastWasSpace = (c == ' ');
            }
        }
        String[] tokens = cleanedLine.toString().trim().split(" ");
        StringBuilder result = new StringBuilder();

        for (String token : tokens) {
            if (isArithmeticExpression1(token)) {
                int evaluatedResult = evaluateExpression(token);
                result.append(evaluatedResult).append(" ");
            } else {
                result.append(token).append(" ");
            }
        }
        return result.toString().trim();
    }

    private static boolean isArithmeticExpression1(String token) {
        if (token.isEmpty()) return false;

        boolean hasDigit = false;
        boolean hasOperator = false;

        for (char c : token.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (isOperator(c)) {
                hasOperator = true;
            } else if (c != '(' && c != ')') {
                return false; 
            }
        }

        return hasDigit && (hasOperator || token.contains("("));
    }
    public static boolean compareResults(String line) {
        String resultWithRegex = processLine(line);
        String resultWithoutRegex = processLine1(line);
         if( resultWithRegex==resultWithoutRegex)
         {
            return true;
         }
         return false;
    }
  
}
