package expression.modes;

import java.math.BigInteger;

public class BigIntegerMode implements Mode<BigInteger> {
    @Override 
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger negate(BigInteger value) {
        return value.negate();
    }

    @Override
    public BigInteger parseConst(String number) {
        return new BigInteger(number);
    }

    @Override
    public BigInteger min(BigInteger left, BigInteger right) {
        return left.min(right);
    }

    @Override
    public BigInteger max(BigInteger left, BigInteger right) {
        return left.max(right);
    }

    @Override
    public BigInteger count(BigInteger value) {
        return BigInteger.valueOf(value.bitCount());
    }
} 