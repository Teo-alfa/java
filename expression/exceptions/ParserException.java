package expression.exceptions;

public abstract class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }
}