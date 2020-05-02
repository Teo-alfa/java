"use strict"

const Expression = (function() {
    const variables = {'x': 0, 'y': 1, 'z': 2};
    function Const(val) {
        this.getVal = () => val;
    }

    Const.prototype.evaluate = function() {
        return this.getVal();
    }

    Const.prototype.diff = function(v) {
        return zero;
    }

    function Variable(name) {
        let index = variables[name];
        this.getName = () => name;
        this.getVal = (...args) => args[index];
    }

    Variable.prototype.evaluate = function(...args) {
        return this.getVal(...args);
    }

    Variable.prototype.diff = function(v) {
        return new Const(v === this.getName() ? 1 : 0);
    }

    function Container(...terms) {
        this.terms = terms;
    }

    Container.prototype.toString = function() {
        return this.terms.join(" ") + " " + this.symbol;
    }

    Container.prototype.prefix = function() {
        return "(" + this.symbol + " "  + this.terms.map(v => v.prefix()).join(" ") + ")";
    }

    Container.prototype.postfix = function() {
        return "(" + this.terms.map(v => v.postfix()).join(" ") + " " + this.symbol + ")";
    }

    Container.prototype.evaluate = function(...args) {
        return this.terms.length == 0 
                ? 0 
                : this.action(...this.terms.map(v => v.evaluate(...args)));
    }

    Container.prototype.diff = function(variable) {
        return this.rule(...this.terms, ...this.terms.map(v => v.diff(variable)));
    }

    const dr = 1 / 100000;
    const one = new Const(1);
    const zero = new Const(0);
    const e = new Const(Math.E);

    function simplediff(ob, x) {
        return (...args) => {
            return (ob.evaluate(...args.map((v, i) => i == variables[x] ? v + dr : v)) 
            - ob.evaluate(...args.map((v, i) => i == variables[x] ? v - dr : v))) / (2 * dr)
        };
    }

    Container.prototype.simplediff = function(x) {
        const diffOp = {};
        diffOp.prototype = this;
        diffOp.evaluate = function(...args) {
            return simplediff(this.prototype, x)(...args);
        }
        diffOp.simplediff = this.simplediff;
        diffOp.diff = function(y) {
            return this.simplediff(y);
        }
        return diffOp;
    }

    function Operation(fun, symbol, diffRule, n) {
        let res = function(...terms) {
            return Container.apply(this, terms);
        }
        res.prototype = Object.create(Container.prototype);
        res.prototype.action = fun;
        res.prototype.symbol = symbol;
        res.prototype.numArg = n == undefined ? fun.length : n;
        if (diffRule != undefined) {
            res.prototype.rule = diffRule;
        } else {
            res.prototype.diff = res.prototype.simplediff;
        }
        return res;
    }

    const Add = Operation((a, b) => a + b, "+", 
                            (f, g, df, dg) => new Add(df, dg));
    const Subtract = Operation((a, b) => a - b, "-", 
                            (f, g, df, dg) => new Subtract(df, dg));
    const Multiply = Operation((a, b) => a * b, "*", 
                            (f, g, df, dg) => new Add(new Multiply(df, g), new Multiply(f, dg)));
    const Divide = Operation((a, b) => a / b, "/", 
                            (f, g, df, dg) => new Divide(new Subtract(new Multiply(df, g), new Multiply(dg, f)), new Multiply(g, g)));
    const Negate = Operation(a => -a, "negate", 
                            (f, df) => new Negate(df));

    const Min3 = Operation(Math.min, "min3", undefined, 3);
    const Max5 = Operation(Math.max, "max5", undefined, 5);
    const Sinh = Operation(Math.sinh, "sinh", (f, df) => new Multiply(new Cosh(f), df));
    const Cosh = Operation(Math.cosh, "cosh", (f, df) => new Multiply(new Sinh(f), df));
    const Ln = Operation(x => Math.log(Math.abs(x)), "ln", (f, df) => new Divide(df, f));
    const Power = Operation(Math.pow, "pow", 
                            (f, g, df, dg) => new Multiply(
                                                    new Power(f, g), 
                                                    new Add(
                                                        new Multiply(
                                                            new Ln(f), 
                                                            dg), 
                                                        new Multiply(
                                                            new Divide(g, f), 
                                                            df))));
    const Log = Operation((x, y) => Math.log(Math.abs(y)) / Math.log(Math.abs(x)), "log", 
                            (f, g, df, dg) => new Divide(
                                                    new Subtract(
                                                        new Divide(
                                                            dg,
                                                            g
                                                        ),
                                                        new Divide(
                                                            new Multiply(
                                                                new Log(f, g),
                                                                df),
                                                            f
                                                        )),
                                                    new Ln(f)
                                                    ));
    const ArcTan = Operation(Math.atan, "atan", (f, df) => new Multiply(
                                                                df, 
                                                                new Divide(
                                                                    one,
                                                                    new Add(one, 
                                                                            new Multiply(f, f))
                                                                )));
    const Exp = Operation(Math.exp, "exp", (f, df) => Power.prototype.rule(e, f, zero, df));
    const Sum = Operation((...terms) => terms.reduce((pr, v) => pr + v), "sum", (...f) => new Sum(...f.splice(-f.length/2)));
    const Avg = Operation((...terms) => terms.reduce((pr, v) => pr + v) / terms.length, "avg", (...f) => new Avg(...f.splice(-f.length/2)));
    const functions = {
        '+': Add,
        '-': Subtract,
        '*': Multiply,
        '/': Divide,
        'negate': Negate,
        'min3' : Min3,
        'max5' : Max5,
        'sinh' : Sinh,
        'cosh' : Cosh,
        'pow' : Power,
        'log' : Log,
        'sinh' : Sinh,
        'cosh' : Cosh,
        'exp' : Exp,
        'atan' : ArcTan,
        'sum' : Sum,
        'avg' : Avg
    }

    // const parseTerm = input => {
    //     return input in variables
    //                 ? new Variable(input) 
    //                 : new Const(+input);
    // }

    const parseTerm = input => {
        if (isNaN(parseFloat(input)) && input[0] != '-') {
            if (!(input in variables)) {
                throw Error("Incorrect variable name: " + input);
            }
        } 
        if (input in variables) {
            return new Variable(input);
        } else {
            if (isNaN(input)) {
                throw Error("Invalid number: " + input);
            }
            return new Const(parseFloat(input));
        }
    }

    const parse = input => {
        return input.trim().split(/\s+/).reduce((prev, curr) => {
            prev.push(curr in functions
                                ? new functions[curr](...prev.splice(-functions[curr].prototype.numArg))
                                : parseTerm(curr));
            return prev;
        }, [])[0];
    }

    const parsePostfix = input => {
        if (input.length == 0) {
            throw Error("Empty input");
        }
        let res = input.replace(/\(|\)/g, u => " " + u + " ")
                        .trim()
                        .split(/\s+/)
                        .reduce((prev, curr) => {
            if (prev.length > 0 && prev[prev.length - 1] in functions && curr != ')') {
                throw Error("Missing closing bracket");
            }
            if (curr === ')') {
                if (!(prev.pop() in functions)) {
                    throw Error("missed operation");
                }
            } else if (curr in functions) {
                if (functions[curr].prototype.numArg != 0 && prev.length <= functions[curr].prototype.numArg) {
                    throw Error("not enough terms");
                }
                let terms = [];
                if (functions[curr].prototype.numArg == 0) {
                    while (prev.length > 0 && prev[prev.length - 1] != '(') {
                        terms.unshift(prev.pop());
                    }
                } else {
                    terms = prev.splice(-functions[curr].prototype.numArg);
                }
                if (prev.length == 0 || prev.pop() != '(') {
                    throw Error("no opening brackets")
                }
                prev.push(new functions[curr](...terms));
                prev.push(curr);
            } else if (curr != '(') {
                let a  = parseTerm(curr);
                if (prev[prev.length - 1] in functions) {
                    throw Error("missing closing bracket");
                }
                prev.push(a);
            } else {
                prev.push(curr);
            }
            return prev;
        }, []);
        if (res.length != 1) {
            throw Error(res.pop() === '(' ? "Missing )" : "Excessive info");
        }
        return res.pop();
    }
    const parsePrefix = input => {
        if (input.length == 0) {
            throw Error("Empty input \"\"");
        }
        let i = 0;
        let array = input.replace(/\(|\)/g, u => " " + u + " ")
                            .trim()
                            .split(/\s+/);
        const parseElement = (input) => {
            let firstspell = input[i++];
            if (firstspell === '(') {
                let operation = input[i++];
                let terms = [];
                if (operation in functions) {
                    let numArg = functions[operation].prototype.numArg;
                    if (input[i] === ')' && numArg != 0) {
                        throw Error("0 arguments received for operation: (" + operation + " [" + numArg + " args])");
                    }
                    for (let k = 0; k < numArg; k++) {
                        try {
                            terms.push(parseElement(input));
                        } catch(e) {
                            throw Error("Wrong number of arguments: expected " 
                            + numArg + " for " + operation 
                            + ", received " + k + " terms");
                        }
                    }
                    while (numArg == 0 && input[i] != ')' && i < input.length) {
                        terms.push(parseElement(input));
                    }
                } else {
                    if (input[i] === ')') {
                        throw Error("Empty term ()");
                    }
                    let unknown = undefined;
                    try {
                        unknown = parseTerm(operation);
                    } catch (e) {
                        throw Error("Unknown operation: " + operation);
                    }
                    let word = unknown instanceof Const 
                                ? "constant" 
                                : unknown instanceof Variable
                                    ? "variable"
                                    : "unknown";
                    throw Error("Expected operation, received: " + word + " " + operation + ", pos:" + i);
                }
                if (input[i++] === ')') {
                    return new functions[operation](...terms);
                } else {
                    --i;
                    try {
                        parseElement(input);
                    } catch (e){
                        throw Error("Missing closing bracket, pos: " + i);
                    }
                    throw Error("Wrong number of arguments: expected " 
                                + functions[operation].prototype.numArg + " for " + operation 
                                + ", received more.");
                }
            } else if (firstspell === ')') {
                throw Error("Unexpected closing bracket, position +-" +  i);
            } else {
                return parseTerm(firstspell);
            }
    
        }

        let res = parseElement(array);
        if (i < array.length) {
            throw Error("Excessive information, pos: " + i);
        }
        return res;
    }
    return {
        Const : Const,
        Variable : Variable,
        Add : Add,
        Subtract : Subtract,
        Multiply : Multiply,
        Divide : Divide,
        Negate : Negate,
        Min3 : Min3,
        Max5 : Max5,
        Log : Log,
        Ln : Ln,
        Power : Power,
        Sinh : Sinh,
        Cosh : Cosh,
        ArcTan : ArcTan,
        Exp : Exp,
        Avg : Avg,
        Sum : Sum,
        parse : parse,
        parsePrefix : parsePrefix,
        parsePostfix : parsePostfix
    }
})();
const 
    Const = Expression.Const,
    Variable = Expression.Variable,
    Add = Expression.Add,
    Subtract = Expression.Subtract,
    Multiply = Expression.Multiply,
    Divide = Expression.Divide,
    Negate = Expression.Negate,
    Min3 = Expression.Min3,
    Max5 = Expression.Max5,
    Log = Expression.Log,
    Ln = Expression.Ln,
    Power = Expression.Power,
    Sinh = Expression.Sinh,
    Cosh = Expression.Cosh,
    ArcTan = Expression.ArcTan,
    Exp = Expression.Exp,
    Sum = Expression.Sum,
    Avg = Expression.Avg,
    parse = Expression.parse,
    parsePrefix = Expression.parsePrefix,
    parsePostfix = Expression.parsePostfix;

Const.prototype.toString = 
Const.prototype.prefix = 
Const.prototype.postfix = function() {
    return this.getVal().toString();
}

Variable.prototype.toString = 
Variable.prototype.prefix = 
Variable.prototype.postfix = function() {
    return this.getName();
}

//println(parsePostfix('((x y - sum)'));
// println("(x 2 +)".replace(/\(|\)/g, u => " " + u + " ").trim().split(/\s+/).map(x => "[" + x + "]"));
// println("(x 2))))) +) ".replace(/\)|\(/g, u => " " + u + " ").trim().split(/\s+/).map(x => "[" + x + "]"));
// println(new Divide(new Negate(new Variable('x')), new Const(2)).postfix());
//println(parsePrefix("(+ x y z)"))