package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Divide<T> extends AbstractBinOperation<T> {
    public Divide(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }
    
    @Override
    public T result(T x, T y) throws ArithmeticException {
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
