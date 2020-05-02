package expression.exceptions;

public class IncorrectSyntaxException extends ParserException {
    public IncorrectSyntaxException(String message) {
        super("Incorrect input expression: " +message);
    }
}