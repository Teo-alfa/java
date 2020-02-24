package expression.parser;

import java.util.*;

import expression.*;


public class ExpressionParser implements Parser {
    String lastOperation;

    private Map<String, Integer> priorityMap = Map.of(
        "+", 2,
        "*", 4,
        "/", 4,
        "-", 2,
        "<<", 1,
        ">>", 1  
    );

    private Set<String> operations = Set.of(
        "+", "*", "/", "-", "<<", ">>"  
    );
    
    private Set<String> unaryOperationSet = Set.of(
        "-"
    );

    private Set<Character> operationSymbols = Set.of(
        '+', '-', '*', '/', '<', '>' 
    );

    @Override
    public TripleExpression parse(String expression) {
        lastOperation = null;
        return parseLevel(new ExpressionSource(expression), null, null, null, 0, false, 0);
    }

    //boolean log = !true;
    protected CommonExpression parseLevel(ExpressionSource source, 
                                            CommonExpression startLeft, 
                                            CommonExpression startRight, 
                                            String startOperation, int startPriority, boolean waitTokean, int level) {
        
        CommonExpression left = startLeft, right = startRight;
        String operation = startOperation;
        while (source.hasNext()) {
                
            String symbol = Character.toString(source.next());
            // if (log) {   
            //     //System.out.println(expression.substring(0, source.position() - 1) +"\u001b[31m" +  (symbol.equals(" ") ? "_" : symbol) + "\u001b[0m" + expression.substring(source.position()));
            //     System.out.println("\u001b[31m{" +  (symbol.equals(" ") ? "_" : symbol) + "}\u001b[0m");
            //     System.out.println("level: " + level);
            //     System.out.println("left: " + left + "\n" + "operation: " + operation + "\n" + "right: " + right + "\n");
            // }
            if (Character.isAlphabetic(symbol.charAt(0))) {
                if (left == null) {
                    left = new Variable(symbol);
                } else {
                    right = new Variable(symbol);
                }
            } else if (Character.isDigit(symbol.charAt(0))){
                if (left == null) {
                    left = constParse(source);
                } else {
                    right = constParse(source);
                }
            } else if (symbol.charAt(0) == '(') {
                if (left == null) {
                    left = parseLevel(source, null, null, null, 0, false, 0);
                } else {
                    right = parseLevel(source, null, null, null, 0, false, 0);
                }  
            } else if (symbol.charAt(0) == ')') {
                lastOperation = null;
                if (level > 0) {
                    source.back();
                }
                return right == null ? left : converter(operation, left, right);
            } else if (operationSymbols.contains(symbol.charAt(0))) {
                symbol = operationParse(source);
                if (unaryOperationSet.contains(symbol) && (left == null || (right == null && operation != null))) {
                    if (Character.isDigit(source.token())) {
                        if (left == null) {
                            left = constParse(source);
                        } else {
                            right = constParse(source);
                        }
                    } else {
                        if (left == null) {
                            left = converter(symbol, null, parseLevel(source, null, null, null, 0, true, 0));
                        } else {
                            right = converter(symbol, null, parseLevel(source, null, null, null, 0, true, 0));
                        }
                    }
                } else {
                    if (operation == null) {
                        operation = symbol;
                    } else {
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
                            // if (log) {
                            //     System.out.println("\u001b[36m" +"dive to the level " + (level) + " --> level " + (level + 1) + "\u001b[0m");
                            // } 

                            right = parseLevel(source, right, null, symbol, priorityMap.get(operation), false, level + 1);
                            
                            // if (log) {
                            //     System.out.println("\u001b[32m" +"rose from the level " + (level + 1) + " --> level " + level + "\u001b[0m");
                            //     System.out.println("left: " + left + "\n" + "operation: " + operation + "\n" + "right: " + right + "\n");
                            //     System.out.println("\u001b[33m" +"symbol: " + symbol +", operation: " + operation + ", lastOperation: " + lastOperation + "\u001b[0m");
                            // } 
                            if (lastOperation != null && priorityMap.get(operation) >= priorityMap.get(lastOperation)) {
                                left = right == null ? left : converter(operation, left, right);
                                right = null;
                                if (level != 0 && priorityMap.get(operation) > priorityMap.get(lastOperation)) {
                                    return left;
                                }
                                operation = lastOperation;
                            } else if (lastOperation != null && priorityMap.get(operation) < priorityMap.get(lastOperation)){
                                right = parseLevel(source, right, null, lastOperation, priorityMap.get(operation), false, level + 1);
                            }
                        }
                    }   
                }      
            }
            if (waitTokean && left != null) {
                return left;
            }
        }
        // if (log) {
        //     System.out.println("In the end: \n" + "left: " + left + "\n" + "operation: " + operation + "\n" + "right: " + right + "\n");
        // }
        return right == null ? left : converter(operation, left, right);
    }

    
    protected Const constParse(ExpressionSource source) {
        source.back();
        StringBuilder value = new StringBuilder();
        if (source.token() == '-') {
            value.append(source.next());
        }
        while (source.hasNext() && Character.isDigit(source.token())) {
            value.append(source.next());
        }
        return new Const(Integer.parseInt(value.toString()));
    }

    private String operationParse(ExpressionSource source) {
        source.back();
        StringBuilder result = new StringBuilder();
        while(source.hasNext() && operationSymbols.contains(source.token())) {
            result.append(source.next());
            if (!isOperationPrefix(operations, result.toString())) {
                result.deleteCharAt(result.length() - 1);
                source.back();
                break;
            }
        }
        return result.toString();
    }

    protected CommonExpression converter(String symbol, CommonExpression op1, CommonExpression op2) {
        switch (symbol) {
            case "+" :
                return new Add(op1, op2);
            case "-" :
                return op1 == null ? new UnaryMinus(op2) : new Subtract(op1, op2);
            case "*" :
                return new Multiply(op1, op2);
            case "/" :
                return new Divide(op1, op2);
            case ">>" :
                return new RightShift(op1, op2);
            case "<<" :
                return new LeftShift(op1, op2);
            default :
                return null;
        }
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