package expression.operations;

import expression.TripleExpression;
import expression.modes.Mode;

public class Count<T> extends AbstractUnaryOperation<T> {

    public Count(TripleExpression<T> value, Mode<T> mode) {
        super(value, mode);
    }
    
    @Override
    public T result(T x){
        return mode.count(x);
    }
    
    @Override
    public String toString() {
        return "count" + this.value;
    }
}