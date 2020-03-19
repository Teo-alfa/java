package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Multiply<T> extends AbstractBinOperation<T> {
    public Multiply(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }

    // @Override
    // public double result(double x, double y) {
    //     return x * y;
    // }

    @Override
    public T result(T x, T y) throws ArithmeticException {
        // if ((x > 0 && y > 0 && Integer.MAX_VALUE / y < x) 
        //     || (x < 0 && y < 0 && 
        //         (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE || Integer.MAX_VALUE / (-y) < (-x)))) {
        //     throw new UpflowException(toMiniString());
        // } else if (!((x > 0 && y > 0) || (x < 0 && y < 0) || x == 0 || y == 0) 
        //             && (x < 0 ? Integer.MIN_VALUE / y > x : Integer.MIN_VALUE / x > y)) {
        //     throw new UnderflowException(toMiniString());
        // }
        return mode.multiply(x, y);
    }

    @Override
    protected int getPriority() {
        return 4;
    }

    @Override
    protected String getSymbol() {
        return "*";
    }

    @Override
    protected boolean isAssociative() {
        return true;
    }
}