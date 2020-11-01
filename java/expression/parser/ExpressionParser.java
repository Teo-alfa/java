package expression.parser;

import java.util.*;

import expression.TripleExpression;
import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;
import expression.modes.*;
import expression.operations.*;


public class ExpressionParser<T> implements Parser<T> {
    String lastOperation;
    Mode<T> mode;

    private Map<String, Integer> priorityMap = Map.of(
        "+", 2,
        "*", 4,
        "/", 4,
        "-", 2,
        "<<", 1,
        ">>", 1, 
        "min", 1,
        "max", 1  
    );

    private Set<String> operations = Set.of(
        "+", "*", "/", "-", "<<", ">>", "log2", "pow2", "min", "max", "count"  
    );
    
    private Set<String> unaryOperationSet = Set.of(
        "-", "log2", "pow2", "count"
    );

    private Set<Character> operationSymbols = Set.of(
        '+', '-', '*', '/', '<', '>', 'l', 'p', 'm', 'c'
    );

    private Set<Character> variableSet = Set.of(
        'x', 'y', 'z'
    );

    private Set<String> longOperations = Set.of(
        "log2", "pow2", "min", "max", "count"
    );

    public ExpressionParser(Mode<T> mode) {
        this.mode = mode;
    }

    @Override
    public TripleExpression<T> parse(String expression) throws ParserException {
        lastOperation = null;
        return parseLevel(new ExpressionSource(expression), null, null, null, 0, false, true, 0);
    }

    protected TripleExpression<T> parseLevel(ExpressionSource source, 
                                            TripleExpression<T> startLeft, 
                                            TripleExpression<T> startRight, 
                                            String startOperation, 
                                            int startPriority, 
                                            boolean waitTokean, 
                                            boolean isBaseLevel, 
                                            int level) throws ParserException {
        
        TripleExpression<T> left = startLeft, right = startRight;
        String operation = startOperation;
        while (source.hasNext()) {  
            String symbol = Character.toString(source.next());
            if (isSingleTerm(symbol.charAt(0))) {
                TripleExpression<T> res = createTerm(symbol.charAt(0), source);
                if (left == null) {
                    left = res;
                } else if (right == null && operation != null) {
                    right = res;
                } else {
                    throw source.error("Unexpected expression: " + res.toString() + "." + 
                                        " Expected: operation" + (symbol.charAt(0) != '(' ? " or closing parenthesis" : "" ) + "." );
                }
            } else if (symbol.charAt(0) == ')') {
                lastOperation = null;
                if (isBaseLevel) {
                    throw source.error("No opening parenthesis");
                }
                if (level > 0) {
                    source.back();
                }
                if (left != null && operation != null && right == null) {
                    throw source.error("No last argument");
                }
                return right == null ? left : converter(operation, left, right);
            } else if (operationSymbols.contains(symbol.charAt(0))) {
                symbol = operationParse(source);
                if (unaryOperationSet.contains(symbol) && (left == null || (right == null && operation != null))) {
                    TripleExpression<T> res = Character.isDigit(source.token()) && symbol.equals("-") 
                                            ? constParse(source) 
                                            : converter(symbol, null, parseLevel(source, null, null, null, 0, true, false, 0));
                    if (left == null) {
                        left = res;
                    } else {
                        right = res;
                    }
                } else {
                    if (left != null && operation == null) {
                        operation = symbol;
                    } else if (left != null && operation != null && right != null) {
                        if (priorityMap.get(operation) >= priorityMap.get(symbol)) {
                            boolean condition = priorityMap.get(operation) > priorityMap.get(symbol);
                            left = right == null ? left : converter(operation, left, right);
                            right = null;
                            operation = symbol;
                            if (startPriority >= priorityMap.get(symbol) && condition) {
                                lastOperation = symbol;
                                return left;
                            }   
                        } else if (priorityMap.get(operation) < priorityMap.get(symbol)) {
                            right = parseLevel(source, right, null, symbol, priorityMap.get(operation), false, false, level + 1);
                            if (lastOperation != null && priorityMap.get(operation) >= priorityMap.get(lastOperation)) {
                                left = right == null ? left : converter(operation, left, right);
                                right = null;
                                if (level != 0 && priorityMap.get(operation) > priorityMap.get(lastOperation)) {
                                    return left;
                                }
                                operation = lastOperation;
                            } else if (lastOperation != null && priorityMap.get(operation) < priorityMap.get(lastOperation)){
                                right = parseLevel(source, right, null, lastOperation, priorityMap.get(operation), false, false, level + 1);
                            }
                        }
                    } else {
                        if (left == null) {
                            throw source.error("No first argument");
                        } else if (right == null) {
                            throw source.error("No middle argument");
                        }
                    }  
                }      
            } else if (!Character.isWhitespace(symbol.charAt(0))) {
                throw source.error("Unknown symbol: " + symbol.charAt(0));
            }
            if (waitTokean && left != null) {
                return left;
            }
        }
        lastOperation = null;
        if (left != null && operation != null && right == null) {
            throw source.error("No last argument");
        }
        if (!isBaseLevel && level == 0) {
            throw source.error("No closing parenthesis");
        }
        if (left == null && operation == null && right == null) {
            throw source.error("Empty expression");
        }
        return right == null ? left : converter(operation, left, right);
    }

    
    protected Const<T> constParse(ExpressionSource source) throws IllegalArgumentException {
        source.back();
        StringBuilder value = new StringBuilder();
        if (source.token() == '-') {
            value.append(source.next());
        }
        while (source.hasNext() && Character.isDigit(source.token())) {
            value.append(source.next());
        }
        T result = null;
        try {
            result = mode.parseConst(value.toString()); 
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("overflow");
        }
        return new Const<T>(result);
    }

    private String operationParse(ExpressionSource source) throws ParserException {
        source.back();
        StringBuilder result = new StringBuilder();
        if (operationSymbols.contains(source.token())) {
            while(source.hasNext()) {
                result.append(source.next());
                if (!isOperationPrefix(operations, result.toString())) {
                    result.deleteCharAt(result.length() - 1);
                    source.back();
                    break;
                }
            }
        }
        if (!operations.contains(result.toString())) {
            throw source.error("Incorrect name of operation: " + result.toString());
        }
        if (longOperations.contains(result.toString()) 
                && (variableSet.contains(source.token())
                    || (Character.isDigit(result.charAt(result.length() - 1)) 
                        && Character.isDigit(source.token())))) {
            throw source.error("Illegal position of variable or not negative number.");
        }
        return result.toString();
    }

    protected TripleExpression<T> converter(String symbol, TripleExpression<T> op1, TripleExpression<T> op2)
            throws IllegalArgumentException {
        switch (symbol) {
            case "+" :
                return new Add<T>(op1, op2, mode);
            case "-" :
                return op1 == null ? new UnaryMinus<T>(op2, mode) : new Subtract<T>(op1, op2, mode);
            case "*" :
                return new Multiply<T>(op1, op2, mode);
            case "/" :
                return new Divide<T>(op1, op2, mode);
            case "min" :
                return new Min<T>(op1, op2, mode);
            case "max" :
                return new Max<T>(op1, op2, mode);
            case "count" :
                return new Count<T>(op2, mode);
            default :
                throw new IllegalArgumentException("Unknown operator: " + symbol);
        }
    }

    private TripleExpression<T> createTerm(char symbol, ExpressionSource source) throws ParserException{
        if (Character.isDigit(symbol)) {
            return constParse(source);
        } else if (Character.isAlphabetic(symbol)) {
            if (!variableSet.contains(symbol)) {
                throw source.error("Unsupported symbol: " + symbol + ".");
            } else {
                return new Variable<T>(Character.toString(symbol));
            }
        } else if (symbol == '(') {
            return parseLevel(source, null, null, null, 0, false, false, 0);
        } 
        return null;
    }

    private boolean isSingleTerm(char symbol) {
        return Character.isDigit(symbol) 
                || (Character.isLetter(symbol) && !isOperationPrefix(operations, Character.toString(symbol))) 
                || symbol == '(';
    }

    protected boolean isOperationPrefix(Set<String> operations, String operation) {
        boolean result = false;
        for(String op : operations) {
            if (op.startsWith(operation)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
}