"use strict";

const Exception = function(message) {
    this.message = message;
}
Exception.prototype = Error.prototype;
Exception.prototype.name = "ExpressionException";

const SubException = function(name) {
    this.name = name;
}
SubException.prototype = Exception.prototype;

const ExceptionCreator = function(name, comparator) {
    const exception = function (...args) {
        Exception.call(this, comparator(args));
    }
    exception.prototype = new SubException(name);
    return exception;
}

const InvalidTokenException = new ExceptionCreator(
	"InvalidTokenException",
	(pos) => "Invalid token at position " + pos
);

const NotMatchedBracketException = new ExceptionCreator(
	"NotMatchedBracketException",
	(pos) => "Not matched bracket at position " + pos
);

const UnexpectedDoubleOperationsException = new ExceptionCreator(
	"UnexpectedDoubleOperationsException",
	(pos) => "Unexpected double operations at position " + pos
);

const MissingOperatorException = new ExceptionCreator(
	"MissingOperatorException",
	(pos) => "Missing operator at position " + pos
);

const NotEnoughArgumentsException = new ExceptionCreator(
	"NotEnoughArgumentsException",
	(pos) => "Not enough arguments at position " + pos
);

const NotEnoughOperationsException = new ExceptionCreator(
	"NotEnoughOperationsException",
	(pos) => "Not enough operations at position " + pos
);

const MissingSpaceException = new ExceptionCreator(
    "MissingSpaceException",
    (pos) => "Missing space at position " + pos
);

const MissingBracketsException = new ExceptionCreator(
    "MissingBracketsException",
    (pos) => "Missing bracket at position " + pos
);

const Operation = function(evaluateComparator, diffComparator, stringOperation) {
	return function Operation(...args) {
		this.evaluateComparator = evaluateComparator;
		this.diffComparator = diffComparator;
		this.args = args;
		this.stringOperation = stringOperation;
	}
}
Operation.prototype.evaluate = function(x, y,z) {
	return this.evaluateComparator(...this.args.map((arg) => arg.evaluate(x, y, z)));
}
Operation.prototype.diff = function(diffVariabelName) {
	return this.diffComparator(...this.args)(diffVariabelName);
}
Operation.prototype.toString = function() {
	return this.args.reduce((answerString, arg) => answerString += (answerString.length === 0 ? "" : " ") + arg.toString(), "") + " " + this.stringOperation;
}

Operation.prototype.postfix = function() {
	return "(" + this.args.reduce((answerString, arg) => answerString += (answerString.length === 0 ? "" : " ") + arg.postfix(), "") + " " + this.stringOperation + ")";
}

const Const = function Const(constValue) {
	this.evaluate = (x, y, z) => Number(constValue);
	this.diff = (variable) => new Const(0);
	this.toString = () => String(constValue);
	this.postfix = () => String(constValue);
}

const Variable = function Variable(variableName) {
	this.evaluate = (x, y, z) => {
		switch(variableName) {
			case "x": return x;
			case "y": return y;
			case "z": return z;
		}
	};
	this.toString = () => variableName;
	this.postfix = () => variableName;
	this.diff = (variable) => {
		return variable === variableName ? new Const(1) : new Const(0);
	}
}

const Add = new Operation((firstExpression, secondExpression) => firstExpression + secondExpression, 
	(firstExpression, secondExpression) => (diffVariabelName) => 
		new Add(firstExpression.diff(diffVariabelName), secondExpression.diff(diffVariabelName)),
	"+");
Add.prototype = Object.create(Operation.prototype);

const Subtract = new Operation((firstExpression, secondExpression) => firstExpression - secondExpression,
	(firstExpression, secondExpression) => (diffVariabelName) => 
		new Subtract(firstExpression.diff(diffVariabelName), secondExpression.diff(diffVariabelName)),
	"-");
Subtract.prototype = Object.create(Operation.prototype);

const Multiply = new Operation((firstExpression, secondExpression) => firstExpression * secondExpression,
	(firstExpression, secondExpression) => (diffVariabelName) =>
	new Add(
			new Multiply(firstExpression.diff(diffVariabelName), secondExpression),
			new Multiply(firstExpression, secondExpression.diff(diffVariabelName))
		),
	"*");
Multiply.prototype = Object.create(Operation.prototype);

const Divide = new Operation((firstExpression, secondExpression) => firstExpression / secondExpression,
	(firstExpression, secondExpression) => (diffVariabelName) =>
	new Divide(
		new Subtract(
			new Multiply(firstExpression.diff(diffVariabelName), secondExpression),
			new Multiply(firstExpression, secondExpression.diff(diffVariabelName))
			), 
		new Multiply(secondExpression, secondExpression)
		),
	"/");
Divide.prototype = Object.create(Operation.prototype);

const Negate = new Operation((expression) => -expression,
	(expression) => (diffVariabelName) => new Negate(expression.diff(diffVariabelName)),
	"negate");
Negate.prototype = Object.create(Operation.prototype);

const EPow = new Operation((expression) => Math.pow(Math.E, expression),
    (expression) => (diffVariabelName) => new Multiply(new EPow(expression), expression.diff(diffVariabelName)),
    "EPow");
EPow.prototype = Object.create(Operation.prototype);

const Sumexp = new Operation((...args) =>
    (args.length === 0 ? 0 : args.reduce((accumulator, arg) => accumulator + Math.pow(Math.E, arg), 0)),
	(...args) => (diffVariabelName) => args.reduce((accumulator, arg) =>
	    new Add(accumulator, new Multiply(new EPow(arg), arg.diff(diffVariabelName))) , new Const(0)),
	"sumexp");
Sumexp.prototype = Object.create(Operation.prototype);

const Softmax = new Operation((...args) => Math.pow(Math.E, args[0]) /
    (args.length === 0 ? 0 : args.reduce((accumulator, arg) => accumulator + Math.pow(Math.E, arg), 0)),
	(...args) => (diffVariabelName) => (new Divide(new EPow(args[0]), new Sumexp(...args))).diff(diffVariabelName),
	"softmax");
Softmax.prototype = Object.create(Operation.prototype);

const isOperation = function(obj) {
	return (obj === "+" || obj === "-" || obj === "*" || obj === "/");
}

const isExpression = function(obj) {
    return (obj instanceof Const || obj instanceof Variable ||obj instanceof Operation);
}

const isDigit = function(obj) {
	return /\d/.test(obj);
}

const isVariable = function(obj) {
	return (obj === "x" || obj === "y" || obj === "z");
}

const isWord = function(obj) {
	return  /[a-zA-Z]/.test(obj);
}

const isWhiteSpace = function(obj) {
    return /\s/.test(obj);
}

const isBracket = function(obj) {
	return (obj === "(" || obj === ")");
}

const getTokens = function(expression) {
	let tokens = [];

	let tmpNumber = "";
	let currChar = "";
	for (let i = 0; i < expression.length; i++) {
		currChar = expression[i];
		if (isWhiteSpace(currChar) || isBracket(currChar)) {
			if (isBracket(currChar)) {
				tokens.push([currChar, i]);
				continue;
			}
		} else if (isWord(currChar)) {
			let currWord = "";
			for (; i < expression.length && isWord(expression[i]); i++) {
				currWord += expression[i];
			}

			i--;

			switch(currWord) {
				case "x": case "y": case "z":
				case "sumexp":
				case "softmax":
				case "negate":
				tokens.push([currWord, i - currWord.length + 1]); break;
				default: throw new InvalidTokenException(i - currWord.length + 1);
			}
		} else if (isOperation(currChar)) {
		    if (currChar === "-" && i != expression.length - 1 && isDigit(expression[i + 1])) {
                tmpNumber = "";
                i++;
                for (; i < expression.length && isDigit(expression[i]); i++) {
                    tmpNumber += expression[i];
                }

                i--;
                tmpNumber = "-" + tmpNumber;

                tokens.push([tmpNumber, i - tmpNumber.length + 1]);
                continue;
		    }

		    if (i != 0 && !isWhiteSpace(expression[i - 1]) && !isBracket(expression[i - 1])) {
                throw new MissingSpaceException(i - 1);
            }

			if (i != expression.length - 1 && !isWhiteSpace(expression[i + 1]) && !isBracket(expression[i + 1])) {
			    throw new MissingSpaceException(i + 1);
			}

            tokens.push([currChar, i]);
		} else if (isDigit(currChar)) {
			tmpNumber = "";
			for (; i < expression.length && isDigit(expression[i]); i++) {
				tmpNumber += expression[i];
			}

			i--;

			tokens.push([tmpNumber, i - tmpNumber.length + 1]);
		} else {
			throw new InvalidTokenException(i);
		}
	}

	return tokens;
}

const parsePostfix = function(expression) {
	let tokens = getTokens(expression);
	let stack = [];

	if (tokens.length === 0) {
	    throw new NotEnoughArgumentsException(0);
	}

	for (let i = 0; i < tokens.length; i++) {

        let token = tokens[i][0];
        let position = tokens[i][1];

        if (token === "(") {
            stack.push("(");
        } else if (token === ")") {
            if (stack.length < 2 || stack[stack.length - 2] !== "(") {
                throw new NotMatchedBracketException(position);
            }

            let rememberedExpression = stack.pop();

            if (!(rememberedExpression instanceof Operation)) {
                throw new NotEnoughOperationsException(position);
            }

            stack.pop();
            stack.push(rememberedExpression);
        } else if (isOperation(token)) {
            if (isExpression(stack[stack.length - 1]) && isExpression(stack[stack.length - 2])) {

                let secondExpression = stack.pop();
                let firstExpression = stack.pop();

                switch(token) {
                    case "+": stack.push(new Add(firstExpression, secondExpression)); break;
                    case "-": stack.push(new Subtract(firstExpression, secondExpression)); break;
                    case "*": stack.push(new Multiply(firstExpression, secondExpression)); break;
                    case "/": stack.push(new Divide(firstExpression, secondExpression)); break;
                }

                if (i !== tokens.length - 1 && tokens[i + 1][0] !== ")") {
                    throw new MissingBracketsException(tokens[i + 1][1]);
                }

            } else {
                throw new UnexpectedDoubleOperationsException(position);
            }
        } else if (isDigit(token)) {
            stack.push(new Const(token));
        } else if (isVariable(token)) {
            stack.push(new Variable(token));
        } else if (token === "sumexp") {
            let stackTop = stack.pop();
            let args = [];

            while (stack.length > 0 && isExpression(stackTop)) {
                args.push(stackTop);

                if (stack.length > 0) {
                    stackTop = stack.pop();
                }
            }

            if (!isExpression(stackTop)) {
                if (!isBracket(stackTop)) {
                    throw new NotEnoughArgumentsException(position);
                }

                stack.push(stackTop);
            }

            stack.push(new Sumexp(...args.reverse()));

            if (i !== tokens.length - 1 && tokens[i + 1][0] !== ")") {
                throw new MissingBracketsException(tokens[i + 1][1]);
            }
        } else if (token === "softmax") {
            let stackTop = stack.pop();
            let args = [];

            while (stack.length > 0 && isExpression(stackTop)) {
                args.push(stackTop);

                if (stack.length > 0) {
                    stackTop = stack.pop();
                }
            }

            if (!isExpression(stackTop)) {
                if (!isBracket(stackTop)) {
                    throw new NotEnoughArgumentsException(position);
                }

                stack.push(stackTop);
            }

            stack.push(new Softmax(...args.reverse()));

            if (i !== tokens.length - 1 && tokens[i + 1][0] !== ")") {
                throw new MissingBracketsException(tokens[i + 1][1]);
            }
        } else if (token === "negate") {
            if (stack.length === 0) {
                throw new NotEnoughArgumentsException(position);
            }

            let stackTop = stack.pop();

            if (!isExpression(stackTop)) {
                throw new NotEnoughArgumentsException(position);
            }

            stack.push(new Negate(stackTop));

            if (i !== tokens.length - 1 && tokens[i + 1][0] !== ")") {
                throw new MissingBracketsException(tokens[i + 1][1]);
            }
        }
	}

	if (stack.length > 1) {
		throw new NotEnoughOperationsException(expression.length - 1);
	}

	return stack.pop();
}
