const pi = (x, y, z) => Math.PI;
const e = (x, y, z) => Math.E;

let solve = comparator => (...args) => {
	return (x, y, z) => {
		let arrayOfFunctions = [];
		for (arg of args) {
			arrayOfFunctions.push(arg(x, y, z));
		}

		return comparator(...arrayOfFunctions);
	}
}

let cnst =  function(constValue) {
	return (x, y, z) => constValue;
}

let variable = function(variableName) {
	return (x, y, z) => {
		switch (variableName) {
			case "x": return x;
			case "y": return y;
			case "z": return z;
		}
	}
}

let getMiddle = function(...args) {
	args.sort((a, b) => {
		if (isNaN(a)) {
			return 1;
		}

		if (isNaN(b)) {
			return -1;
		}

		return Number(a) - Number(b);
	});

	return args[Math.floor(args.length / 2)];
}

let add = solve((a, b) => a + b);
let subtract = solve((a, b) => a - b);
let multiply = solve((a, b) => a * b);
let divide = solve((a, b) => a / b);
let negate = solve(a => -a);
let avg5 = solve((a, b, c, d, e) => (a + b + c + d + e) / 5.0);
let med3 = solve((a, b, c) => getMiddle(a, b, c));

let getTokens = function(expression) {
	let tokens = [];

	let token = "";
	for (char of expression) {
		if (char === " ") {
			if (token !== "") {
				tokens.push(token);
			}

			token = "";
		} else {
			token += char;
		}
	}

	if (token !== "") {
		tokens.push(token);
	}

	return tokens;
}

let parse = function(expression) {
	let tokens = getTokens(expression);
	let stack = [];

	for (token of tokens) {
		switch(token) {
			case "+": stack.push(add(stack.pop(), stack.pop())); break;
			case "-": {
				let firstFunc = stack.pop();
				let secondFucn = stack.pop();
				
				stack.push(subtract(secondFucn, firstFunc)); 
				break;
			};
			case "*": stack.push(multiply(stack.pop(), stack.pop())); break;
			case "/": {
				let firstFunc = stack.pop();
				let secondFucn = stack.pop();

				stack.push(divide(secondFucn, firstFunc)); 
				break;
			};
			case "x": case "y": case "z": stack.push(variable(token)); break;
			case "negate": stack.push(negate(stack.pop())); break;
			case "pi": stack.push(pi); break;
			case "e": stack.push(e); break;
			case "avg5": {
				let functions = [];
				for (let i = 0; i < 5; i++) {
					functions.push(stack.pop());
				}

				stack.push(avg5(...functions));
				break;
			};
			case "med3": {
				let functions = [];
				for (let i = 0; i < 3; i++) {
					functions.push(stack.pop());
				}

				stack.push(med3(...functions));
				break;
			};
			default: stack.push(cnst(Number(token))); break;
		}
	}

	return stack.pop();
}
