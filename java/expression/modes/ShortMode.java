package expression.modes;

public class ShortMode implements Mode<Short> {
    @Override 
    public Short add(Short left, Short right) {
        return (short) (left + right);
    }

    @Override
    public Short subtract(Short left, Short right) {
        return (short) (left - right);
    }

    @Override
    public Short multiply(Short left, Short right) {
        return (short) (left * right);
    }

    @Override
    public Short divide(Short left, Short right) {
        return (short) (left / right);
    }

    @Override
    public Short negate(Short value) {
        return (short) -value;
    }

    @Override
    public Short parseConst(String number) {
        return (short)Integer.parseInt(number);
    }

    @Override
    public Short min(Short left, Short right) {
        return left > right ? right : left;
    }

    @Override
    public Short max(Short left, Short right) {
        return left > right ? left : right;
    }

    @Override
    public Short count(Short value) {
        return (short) Integer.bitCount(value & 0xffff);
    }    
} 