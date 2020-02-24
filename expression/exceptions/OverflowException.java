package expression.exceptions;

public class OverflowException extends ArithmeticException {
    public OverflowException(String message) {
        super("Overflow value: " + message);
    }
}