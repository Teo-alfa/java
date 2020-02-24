package expression.exceptions;

import expression.CommonExpression;
import expression.Const;
import expression.Pow2;

public class CheckedPower extends Pow2 {
    public CheckedPower(CommonExpression op1) {
        super(op1);
    }

    public int result(int x) throws ArithmeticException{
        if (x < 0) {
            throw new IllegalStateException(Integer.toString(x));
        } else if ( x > new CheckedLogarithm(new Const(Integer.MAX_VALUE)).evaluate(0, 0, 0)){
            throw new OverflowException("Pow2(" + Integer.toString(x) + ")");
        }

        return super.result(x);
    }
}