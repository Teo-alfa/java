package expression.exceptions;

import expression.CommonExpression;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(CommonExpression op1, CommonExpression op2) {
        super(op1, op2);
    }

    public int result(int x, int y) throws ArithmeticException {
        if (y == 0) {
            throw new DBZException(toMiniString());
        } else if (x == Integer.MIN_VALUE && y == -1){
            throw new OverflowException(toMiniString());
        }
        return super.result(x, y);
    }
}