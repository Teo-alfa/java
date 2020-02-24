package expression.exceptions;

import expression.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser {
    TripleExpression parse(String expression) throws ParserException;
}