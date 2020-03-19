package expression.generic;

import java.util.Map;

import expression.TripleExpression;
import expression.parser.ExpressionParser;
import expression.modes.*;
import expression.exceptions.ArithmeticException;

public class GenericTabulator implements Tabulator {
    public Map<String, Mode<?>> modeMap = Map.of(
        "i", new CheckedIntegerMode(),
        "d", new DoubleMode(),
        "bi", new BigIntegerMode(),
        "u", new UncheckedIntegerMode(),
        "l", new LongMode(),
        "s", new ShortMode()
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
            throws Exception {
        Mode<?> calculateMode = modeMap.get(mode);
        return tabulateWithMode(calculateMode, expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] tabulateWithMode(Mode<T> calculateMode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
            throws Exception {

        ExpressionParser<T> parser = new ExpressionParser<>(calculateMode);
        TripleExpression<T> expr = parser.parse(expression);
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = x1; i <= x2; i++) {
            for(int j = y1; j <= y2; j++) {
                for(int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = expr.evaluate(calculateMode.parseConst(Integer.toString(i)), 
                                                    calculateMode.parseConst(Integer.toString(j)), 
                                                    calculateMode.parseConst(Integer.toString(k))); 
                    } catch (ArithmeticException | java.lang.ArithmeticException e) {

                    }
                }
            }
        }
        return result;
    }
}