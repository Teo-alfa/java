package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class UnaryMinus<T> extends AbstractUnaryOperation<T> {

    public UnaryMinus(TripleExpression<T> value, Mode<T> mode) {
        super(value, mode);
    }
    
    @Override
    public T result(T x){
        return mode.negate(x);
    }
    
    @Override
    public String toString() {
        return "-" + this.value;
    }
}