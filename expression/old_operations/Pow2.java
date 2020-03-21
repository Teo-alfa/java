// package expression;

// public class Pow2 extends AbstractUnaryOperation {
//     public Pow2(CommonExpression value) {
//         super(value);
//     }

//     @Override
//     public double result(double x) {
//         return Math.pow(2, x);
//     }

//     @Override
//     public int result(int x){
//         int count = 1;
//         for(int i = 0; i < x; i++) {
//             count *= 2;
//         }
//         return count;
//     }
    
//     @Override
//     public String toString() {
//         return "pow2(" + this.value + ")";
//     }
// }