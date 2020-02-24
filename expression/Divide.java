package expression;

public class Divide extends AbstractBinOperation {
    public Divide(CommonExpression op1, CommonExpression op2) {
        super(op1, op2);
    }

    @Override
    public double result(double x, double y) {
        return x / y;
    }

    @Override
    public int result(int x, int y) throws ArithmeticException {
        // if (y == 0) {
        //     throw new DBZException(toMiniString());
        // } else if (x == Integer.MIN_VALUE && y == -1){
        //     throw new UpflowException(toMiniString());
        // }
        return x / y;
    }

    @Override
    protected int getPriority() {
        return 4;
    }

    @Override
    protected String getSymbol() {
        return "/";
    }

    @Override
    protected boolean isAssociative() {
        return false;
    }
}
