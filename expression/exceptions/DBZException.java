package expression.exceptions;

public class DBZException extends ArithmeticException {
    public DBZException(String message) {
        super("Divide by zero: " + message);
    }
}