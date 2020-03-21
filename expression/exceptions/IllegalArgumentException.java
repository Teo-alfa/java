package expression.exceptions;

public class IllegalArgumentException extends ParserException {
    public IllegalArgumentException(String message) {
        super("Illegal argument " + message);
    }
}