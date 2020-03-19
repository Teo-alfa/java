package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Divide<T> extends AbstractBinOperation<T> {
    public Divide(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }

    // @Override
    // public double result(double x, double y) {
    //     return x / y;
    // }

    @Override
    public T result(T x, T y) throws ArithmeticException {
        // if (y == 0) {
        //     throw new DBZException(toMiniString());
        // } else if (x == Integer.MIN_VALUE && y == -1){
        //     throw new UpflowException(toMiniString());
        // }
        return mode.divide(x, y);
    }

    @Override
    protected int getPriority() {
        return 4;
    }

    @Override
    protected String getSymbol() {
        return "/";
    }

    @Override
    protected boolean isAssociative() {
        return false;
    }
}
