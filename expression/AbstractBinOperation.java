package expression;

import java.util.Objects;

public abstract class AbstractBinOperation implements CommonExpression {
    private CommonExpression op1, op2;
    
    public AbstractBinOperation(CommonExpression op1, CommonExpression op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    protected abstract int getPriority();
    protected abstract String getSymbol();
    protected abstract boolean isAssociative();
    protected abstract double result(double x, double y);
    protected abstract int result(int x, int y);

    @Override 
    public double evaluate(double x){
        return result(op1.evaluate(x), op2.evaluate(x));
    }

    @Override 
    public int evaluate(int x) {
        return result(op1.evaluate(x), op2.evaluate(x));
    }

    @Override 
    public int evaluate(int x, int y, int z) {
        return result(op1.evaluate(x, y, z), op2.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + op1 + " " + getSymbol() + " " + op2 + ")";
    }

    
    @Override
    public String toMiniString() {
        return toMiniStringPart(op1, needParenthesis(op1)) + " " + getSymbol() + " " + 
                toMiniStringPart(op2, needParenthesis(op2) || op2 instanceof AbstractBinOperation && addCndForRightPart((AbstractBinOperation)op2));
    }

    private boolean needParenthesis(CommonExpression op) {
        return (op instanceof AbstractBinOperation) && ((AbstractBinOperation) op).getPriority() < this.getPriority();
    }

    public String toMiniStringPart(CommonExpression op, boolean additionalCondition) {
        return (additionalCondition) ? "(" + op.toMiniString() + ")" : op.toMiniString();
    }

    public boolean addCndForRightPart(AbstractBinOperation abstractOp) {
        return abstractOp.getPriority() == this.getPriority() 
                && !((isAssociative())
                    && abstractOp.getClass() == getClass());
    }
    

    @Override
    public int hashCode() {
        return Objects.hash(op1, op2, getClass());
    }

    @Override
    public boolean equals(Object object) {
        return object != null 
                && object.getClass() == getClass() 
                && equalsParts((AbstractBinOperation) object);                                
    }
    
    protected boolean equalsParts(AbstractBinOperation operation) {
        return op1.equals(operation.op1) && op2.equals(operation.op2);
    }
}
