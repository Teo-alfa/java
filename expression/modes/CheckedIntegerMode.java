package expression.modes;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;
import expression.exceptions.UnderflowException;

public class CheckedIntegerMode implements Mode<Integer> {
    @Override 
    public Integer add(Integer left, Integer right) {
        if (left > 0 && right > 0 && Integer.MAX_VALUE - left < right) {
            throw new OverflowException("");
        } else if (left < 0 && right < 0 && Integer.MIN_VALUE - left > right){
            throw new UnderflowException("");
        }
        return left + right;
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        if (left >= 0 && right < 0 && Integer.MAX_VALUE + right < left) {
            throw new OverflowException("");
        } else if (left < 0 && right > 0 && Integer.MIN_VALUE + right > left){
            throw new UnderflowException("");
        }
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) {
        if ((left > 0 && right > 0 && Integer.MAX_VALUE / right < left) 
            || (left < 0 && right < 0 && 
                (left == Integer.MIN_VALUE || right == Integer.MIN_VALUE || Integer.MAX_VALUE / (-right) < (-left)))) {
            throw new OverflowException("");
        } else if (!((left > 0 && right > 0) || (left < 0 && right < 0) || left == 0 || right == 0) 
                    && (left < 0 ? Integer.MIN_VALUE / right > left : Integer.MIN_VALUE / left > right)) {
            throw new UnderflowException("");
        }
        return left * right;
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        if (right == 0) {
            throw new DBZException("");
        } else if (left == Integer.MIN_VALUE && right == -1){
            throw new OverflowException("");
        }
        return left / right;
    }

    @Override
    public Integer negate(Integer value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("");
        }
        return -value;
    }

    @Override
    public Integer parseConst(String number) {
        return Integer.parseInt(number);
    }

    @Override
    public Integer min(Integer left, Integer right) {
        return Integer.min(left, right);
    }

    @Override
    public Integer max(Integer left, Integer right) {
        return Integer.max(left, right);
    }

    @Override
    public Integer count(Integer value) {
        return Integer.bitCount(value);
    }

    
} 