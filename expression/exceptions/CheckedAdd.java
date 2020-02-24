package expression.exceptions;

import expression.Add;
import expression.CommonExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(CommonExpression op1, CommonExpression op2) {
        super(op1, op2);
    }

    public int result(int x, int y) throws ArithmeticException {
        if (x > 0 && y > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException(toMiniString());
        } else if (x < 0 && y < 0 && Integer.MIN_VALUE - x > y){
            throw new UnderflowException(toMiniString());
        }
        return super.result(x, y);
    }
}