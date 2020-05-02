package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Max<T> extends AbstractBinOperation<T> {
    public Max(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }

    @Override
    public T result(T x, T y) throws ArithmeticException {
        return mode.max(x, y);
    }

    @Override
    protected int getPriority() {
        return 1;
    }

    @Override
    protected String getSymbol() {
        return "max";
    }

    @Override
    protected boolean isAssociative() {
        return true;
    }
}