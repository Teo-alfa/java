"use strict";

const variables = {'x': 0, 'y': 1, 'z': 2};

const cnst = value => () => value;
const variable = name => {
    let index = variables[name];
    return (...args) => {
        return args[index];
    }
};

const pi = cnst(Math.PI);
const e = cnst(Math.E);

function multyFun(f, n) {
    let realf = (...terms) => (...args) => f(...terms.map(curr => curr(...args)));
    realf.numArg = n || f.length;
    return realf;
}

const add = multyFun((x, y) => x + y);
const subtract = multyFun((x, y) => x - y);
const multiply = multyFun((x, y) => x * y);
const divide = multyFun((x, y) => x / y);
const negate = multyFun(x => -x);
const cube = multyFun(x => x * x * x);
const cuberoot = multyFun(x => Math.cbrt(x));

const avg5 = multyFun(function (...values) {
    let res = values.reduce((prev, curr) => prev + curr);
    return res / values.length;
}, 5);

const med3 = multyFun(function (...values) {
    values.sort((a, b) => a - b);
    return values[Math.floor(values.length / 2)];
}, 3);

const functions = {
    '+': add,
    '-': subtract,
    '*': multiply,
    '/': divide,
    'negate': negate,
    'med3': med3,
    'avg5' : avg5,
    'cube' : cube,
    'cuberoot' : cuberoot
}

const cnsts = {
    'pi' : pi,
    'e': e
}
    
const parseTerm = input => {
    return input in variables
                ? variable(input) 
                : input in cnsts
                    ? cnsts[input]
                    : cnst(+input);
}

const parse = input => {
    return input.trim().split(/\s+/).reduce((prev, curr) => {
        prev.push(curr in functions
                            ? functions[curr](...prev.splice(-functions[curr].numArg))
                            : parseTerm(curr));
        return prev;
    }, [])[0];
}

