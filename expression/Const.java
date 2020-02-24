package expression;

import java.util.Objects;

public class Const implements CommonExpression {
    private Number value;

    public Const(Number value) {
        this.value = value;
    } 
    
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public double evaluate(double x) {
        return value.doubleValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return evaluate(x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, getClass());
    }

    @Override
    public boolean equals(Object object) {
        return object != null 
                && object.getClass() == this.getClass()
                && this.value.equals(((Const)object).value);
    }
}