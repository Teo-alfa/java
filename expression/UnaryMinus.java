package expression;

public class UnaryMinus extends AbstractUnaryOperation {

    public UnaryMinus(CommonExpression value) {
        super(value);
    }

    @Override
    public double result(double x) {
        return -x;
    }

    @Override
    public int result(int x){
        return -x;
    }
    
    @Override
    public String toString() {
        return "-" + this.value;
    }
}