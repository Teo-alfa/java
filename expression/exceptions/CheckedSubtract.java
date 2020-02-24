package expression.exceptions;

import expression.CommonExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(CommonExpression op1, CommonExpression op2) {
        super(op1, op2);
    }

    public int result(int x, int y) throws ArithmeticException{
        if (x >= 0 && y < 0 && Integer.MAX_VALUE + y < x) {
            throw new OverflowException(toMiniString());
        } else if (x < 0 && y > 0 && Integer.MIN_VALUE + y > x){
            throw new UnderflowException(toMiniString());
        }
        return super.result(x, y);
    }
}