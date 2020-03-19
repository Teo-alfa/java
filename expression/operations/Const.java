package expression.operations;

import java.util.Objects;

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

    // @Override
    // public int evaluate(int x) {
    //     return value.intValue();
    // }

    // @Override
    // public double evaluate(double x) {
    //     return value.doubleValue();
    // }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, getClass());
    }

    @Override
    public boolean equals(Object object) {
        return object != null 
                && object.getClass() == this.getClass()
                && this.value.equals(((Const<T>)object).value);
    }
}