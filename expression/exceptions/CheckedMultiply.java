package expression.exceptions;

import expression.CommonExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(CommonExpression op1, CommonExpression op2) {
        super(op1, op2);
    }

    public int result(int x, int y) {
        if ((x > 0 && y > 0 && Integer.MAX_VALUE / y < x) 
            || (x < 0 && y < 0 && 
                (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE || Integer.MAX_VALUE / (-y) < (-x)))) {
            throw new OverflowException(toMiniString());
        } else if (!((x > 0 && y > 0) || (x < 0 && y < 0) || x == 0 || y == 0) 
                    && (x < 0 ? Integer.MIN_VALUE / y > x : Integer.MIN_VALUE / x > y)) {
            throw new UnderflowException(toMiniString());
        }
        return super.result(x, y);
    }
}