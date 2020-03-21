package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Add<T> extends AbstractBinOperation<T> {
    public Add(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }

    // @Override
    // public double result(double x, double y) {
    //     return x + y;
    // }

    @Override
    public T result(T x, T y) {
        // if (x > 0 && y > 0 && Integer.MAX_VALUE - x < y) {
        //     throw new UpflowException(toMiniString());
        // } else if (x < 0 && y < 0 && Integer.MIN_VALUE - x > y){
        //     throw new UnderflowException(toMiniString());
        // }
        return mode.add(x, y);
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected String getSymbol() {
        return "+";
    }

    @Override
    protected boolean isAssociative() {
        return true;
    }
}