package expression;

import expression.parser.*;
import expression.generic.GenericTabulator;
import expression.modes.CheckedIntegerMode;

public class Main {
    public static void main(String[] args) {
        System.out.println(Integer.MIN_VALUE >> 1);
        Parser<Integer> parser = new ExpressionParser<Integer>(new CheckedIntegerMode());
        String expression = "10";
        System.out.println("expression: " + expression);
        try {
            System.out.println(parser.parse(expression));
            int x = 1, y = 0, z = 0;
            System.out.println("evaluate x = " + x + ", y = " + y + ", z = " + z + ": "
                    + parser.parse(expression).evaluate(x, y, z));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        GenericTabulator g = new GenericTabulator();
        Object[][][] a = null;
        try {
            a = g.tabulate("i", expression, -1, 12, -6, 14, -11, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //System.out.println(new CheckedMultiply(new CheckedDivide(new Const(1), new Const(0)), new Const(Integer.MIN_VALUE)).evaluate(1, 4, 1));
        
    }
}