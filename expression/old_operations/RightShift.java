// package expression;

// public class RightShift extends AbstractBinOperation {
//     public RightShift(CommonExpression op1, CommonExpression op2) {
//         super(op1, op2);
//     }

//     @Override
//     public double result(double x, double y) {
//         return (int)x >> (int)y;
//     }

//     @Override
//     public int result(int x, int y) {
//         return x >> y;
//     }

//     @Override
//     protected int getPriority() {
//         return 1;
//     }

//     @Override
//     protected String getSymbol() {
//         return ">>";
//     }

//     @Override
//     protected boolean isAssociative() {
//         return true;
//     }
// }