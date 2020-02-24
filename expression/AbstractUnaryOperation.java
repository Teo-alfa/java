package expression;

import java.util.Objects;

public abstract class AbstractUnaryOperation implements CommonExpression {
    protected CommonExpression value;

    public AbstractUnaryOperation(CommonExpression value) {
        this.value = value;
    }

    protected abstract double result(double x);
    protected abstract int result(int x);

    @Override 
    public double evaluate(double x) {
        return result(value.evaluate(x));
    }

    @Override 
    public int evaluate(int x) {
        return result(value.evaluate(x));
    }

    @Override 
    public int evaluate(int x, int y, int z) {
        return result(value.evaluate(x, y, z));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, getClass());
    }

    @Override
    public boolean equals(Object object) {
        return object != null 
                && object.getClass() == getClass() 
                && value.equals(((AbstractUnaryOperation) object).value);                                
    }

}