package expression.exceptions;

import expression.CommonExpression;
import expression.UnaryMinus;

public class CheckedNegate extends UnaryMinus {
    public CheckedNegate(CommonExpression op) {
        super(op);
    }

    public int result(int x) throws ArithmeticException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException(toMiniString());
        }
        return -x;
    }
}