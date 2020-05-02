// package expression;

// public class Log2 extends AbstractUnaryOperation {
//     public Log2(CommonExpression value) {
//         super(value);
//     }

//     @Override
//     public double result(double x) {
//         return Math.log(x) / Math.log(2);
//     }

//     @Override
//     public int result(int x){
//         int count = 0;
//         while (x > 1) {
//             x /= 2;
//             count++;
//         }
//         return count;
//     }
    
//     @Override
//     public String toString() {
//         return "log2(" + this.value + ")";
//     }
// }