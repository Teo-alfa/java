package expression.exceptions;

public class UnderflowException extends ArithmeticException {
    public UnderflowException(String message) {
        super("Underflow value: " + message);
    }
}