package expression.exceptions;

public class IllegalArgumentException extends ArithmeticException {
    public IllegalArgumentException(String message) {
        super("Illegal argument " + message);
    }
}