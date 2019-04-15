"use strict";

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
	return this.args.reduce((answerString, arg) => answerString += (answerString.length === 0 ? "" : " ") + arg.toString()) + " " + this.stringOperation;
}

const Const = function Const(constValue) {
	this.evaluate = (x, y, z) => constValue;
	this.diff = (variable) => new Const(0);
	this.toString = () => String(constValue);
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

const ArcTan = new Operation((expression) => Math.atan(expression),
	(expression) => (diffVariabelName) => 
		new Divide(
			expression.diff(diffVariabelName), 
			new Add(
				new Const(1), 
				new Multiply(expression, expression)
			)
		),
	"atan"
);
ArcTan.prototype = Object.create(Operation.prototype);

const ArcTan2 = new Operation((firstExpression, secondExpression) => Math.atan2(firstExpression, secondExpression),
	(firstExpression, secondExpression) => (diffVariabelName) => 
		new Divide(
			new Subtract(
				new Multiply(firstExpression.diff(diffVariabelName), secondExpression),
				new Multiply(firstExpression, secondExpression.diff(diffVariabelName))
				), 
			new Add(
				new Multiply(firstExpression, firstExpression),
				new Multiply(secondExpression, secondExpression)
			)
		),
	"atan2"
);
ArcTan2.prototype = Object.create(Operation.prototype);

const getTokens = function(expression) {
	return expression.split(" ").filter(word => word !== "");
}

const parse = function(expression) {
	let tokens = getTokens(expression);
	let stack = [];

	tokens.map((token) => {
		switch(token) {
			case "+": {
				let firstFunction = stack.pop();
				let secondFunction = stack.pop();

				stack.push(new Add(secondFunction, firstFunction)); 
				break;
			};
			case "-": {
				let firstFunction = stack.pop();
				let secondFunction = stack.pop();
				
				stack.push(new Subtract(secondFunction, firstFunction)); 
				break;
			};
			case "*": {
				let firstFunction = stack.pop();
				let secondFunction = stack.pop();
				
				stack.push(new Multiply(secondFunction, firstFunction)); 
				break;
			};
			case "/": {
				let firstFunction = stack.pop();
				let secondFunction = stack.pop();

				stack.push(new Divide(secondFunction, firstFunction)); 
				break;
			};
			case "negate": stack.push(new Negate(stack.pop())); break;
			case "x": case "y": case "z": stack.push(new Variable(token)); break;
			case "atan": stack.push(new ArcTan(stack.pop())); break;
			case "atan2": {
				let firstFunction = stack.pop();
				let secondFunction = stack.pop();
				
				stack.push(new ArcTan2(secondFunction, firstFunction)); 
				break;
			}
			default: stack.push(new Const(Number(token))); break;
		}
	});

	return stack.pop();
}
