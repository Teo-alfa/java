package expression.modes;

public class DoubleMode implements Mode<Double> {
    @Override 
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double negate(Double value) {
        return -value;
    }

    @Override
    public Double parseConst(String number) {
        return Double.parseDouble(number);
    }

    @Override
    public Double min(Double left, Double right) {
        return Double.min(left, right);
    }

    @Override
    public Double max(Double left, Double right) {
        return Double.max(left, right);
    }

    @Override
    public Double count(Double value) {
        return (double)Long.bitCount(Double.doubleToLongBits(value));
    }
} 