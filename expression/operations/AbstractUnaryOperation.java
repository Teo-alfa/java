package expression.operations;

import java.util.Objects;

import expression.TripleExpression;
import expression.modes.Mode;

public abstract class AbstractUnaryOperation<T> implements TripleExpression<T> {
    protected TripleExpression<T> value;
    protected Mode<T> mode;

    public AbstractUnaryOperation(TripleExpression<T> value, Mode<T> mode) {
        this.value = value;
        this.mode = mode;
    }

    //protected abstract double result(double x);
    protected abstract T result(T x);

    // @Override 
    // public double evaluate(double x) {
    //     return result(value.evaluate(x));
    // }

    // @Override 
    // public int evaluate(int x) {
    //     return result(value.evaluate(x));
    // }

    @Override 
    public T evaluate(T x, T y, T z) {
        return result(value.evaluate(x, y, z));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, getClass());
    }

    // @Override
    // public boolean equals(Object object) {
    //     return object != null 
    //             && object.getClass() == getClass() 
    //             && value.equals(((AbstractUnaryOperation) object).value);                                
    // }

}