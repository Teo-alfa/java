package expression.operations;

import expression.TripleExpression;

public class Const<T> implements TripleExpression<T> {
    private T value;

    public Const(T value) {
        this.value = value;
    } 
    
    @Override
    public String toString() {
        return value.toString();
    }
    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }
}