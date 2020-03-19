package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Subtract<T> extends AbstractBinOperation<T> {
    public Subtract(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }

    // @Override
    // public double result(double x, double y) {
    //     return x - y;
    // }

    @Override
    public T result(T x, T y) {
        // if (x >= 0 && y < 0 && Integer.MAX_VALUE + y < x) {
        //     throw new UpflowException(toMiniString());
        // } else if (x < 0 && y > 0 && Integer.MIN_VALUE + y > x){
        //     throw new UnderflowException(toMiniString());
        // }
        return mode.subtract(x, y);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected boolean isAssociative() {
        return false;
    }
}