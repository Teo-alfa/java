package expression;


import expression.exceptions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(Integer.MIN_VALUE >> 1);
        Parser parser = new ExpressionParser();
        String expression = "2 + x * 4 - (y * 5 + 6)";
        System.out.println("expression: " + expression);
        try {
           System.out.println(parser.parse(expression)); 
           int x = 1, y = 0, z = 0;
           System.out.println("evaluate x = " + x + ", y = " + y + ", z = " + z + ": " + parser.parse(expression).evaluate(x, y, z)); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        //System.out.println(new CheckedMultiply(new CheckedDivide(new Const(1), new Const(0)), new Const(Integer.MIN_VALUE)).evaluate(1, 4, 1));
        
    }
}