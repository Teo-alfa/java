package expression.operations;

import java.util.Objects;

import expression.TripleExpression;

public class Variable<T> implements TripleExpression<T> {
    private final String name;

    public Variable(String str) {
        this.name = str;
    }
    
    @Override
    public String toString() {
        return name;
    }

    // @Override
    // public int evaluate(int x) {
    //     return evaluate(x, x, x);
    // }

    // @Override
    // public double evaluate(double x) {
    //     return x;
    // }

    @Override
    public T evaluate(T x, T y, T z) {
        return 
            name.equals("x") ? x : 
            name.equals("y") ? y : 
            name.equals("z") ? z : z;
    }
    
    // @Override
    // public boolean equals(Object object) {
    //     return object != null 
    //             && object.getClass() == this.getClass()
    //             && this.name.equals(((Variable<T>) object).name);
    // }

    @Override
    public int hashCode() {
        return Objects.hash(name, getClass());
    }
}