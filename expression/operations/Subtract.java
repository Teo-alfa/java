package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Subtract<T> extends AbstractBinOperation<T> {
    public Subtract(TripleExpression<T> op1, TripleExpression<T> op2, Mode<T> mode) {
        super(op1, op2, mode);
    }
    
    @Override
    public T result(T x, T y) {
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