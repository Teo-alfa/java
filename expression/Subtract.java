package expression;

public class Subtract extends AbstractBinOperation {
    public Subtract(CommonExpression op1, CommonExpression op2) {
        super(op1, op2);
    }

    @Override
    public double result(double x, double y) {
        return x - y;
    }

    @Override
    public int result(int x, int y) {
        // if (x >= 0 && y < 0 && Integer.MAX_VALUE + y < x) {
        //     throw new UpflowException(toMiniString());
        // } else if (x < 0 && y > 0 && Integer.MIN_VALUE + y > x){
        //     throw new UnderflowException(toMiniString());
        // }
        return x - y;
    }

    @Override
    protected int getPriority() {
        return 2;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    protected boolean isAssociative() {
        return false;
    }
}