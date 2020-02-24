package expression.exceptions;

import expression.CommonExpression;
import expression.Log2;

public class CheckedLogarithm extends Log2 {
    public CheckedLogarithm(CommonExpression op1) {
        super(op1);
    }

    public int result(int x) throws ArithmeticException{
        //
        if (x <= 0) {
            throw new IllegalStateException(Integer.toString(x));
        }
        return super.result(x);
    }
}