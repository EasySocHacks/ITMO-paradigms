package expression.parser;

import expression.Abs;
import expression.CheckedAdd;
import expression.Const;
import expression.Mod;
import expression.Square;
import expression.CheckedDivide;
import expression.CheckedMultiply;
import expression.CheckedSubtract;
import expression.TripleExpression;
import expression.CheckedNegate;
import expression.Variable;
import expression.exceptions.DoubleBinaryOperationsException;
import expression.exceptions.IllegalExpressionException;
import expression.exceptions.NotMatchedBracketsException;
import expression.exceptions.Parser;
import expression.exceptions.UnexpectedSymbolException;
import expression.exceptions.ExpressionNotFoundException;
import expression.exceptions.UnexpectedVariableNameException;
import expression.exceptions.UnexpectedVariableValueException;
import expression.generic.Number;

public class ExpressionParser<T> implements Parser<T> {
	private Token currentToken;
	private Token previousToken;
	private int skipChars;
	private StringBuilder expression;
	private StringBuilder numericVariable;
	private int balance;
	private final String minValueString = String.valueOf(Integer.MIN_VALUE);
	private boolean usedUnaryMinus;
	private Number<T> instance;
	
	public ExpressionParser(Number <T> instance) {
		this.instance = instance;
	}
	
	private void nextToken() throws IllegalExpressionException {
		previousToken = currentToken;
		currentToken = Token.ERROR;
		
		while (skipChars < expression.length() && Character.isWhitespace(expression.charAt(skipChars))) {
			skipChars++;
		}
		if (skipChars >= expression.length()) {
			currentToken = Token.END;
			return;
		}
		
		char currentChar = expression.charAt(skipChars);
		
		if (Character.isDigit(currentChar)) {
			currentToken = Token.CONSTANT;
			
			numericVariable.setLength(0);
			numericVariable.append(currentChar);
			
			for (skipChars++; skipChars < expression.length() && 
			Character.isDigit(expression.charAt(skipChars)); skipChars++) {
				numericVariable.append(expression.charAt(skipChars));
			}
			
			return;
		}
		
		if (Character.isLetter(currentChar)) {
			currentToken = Token.VARIABLE;
			
			numericVariable.setLength(0);
			numericVariable.append(currentChar);
			
			for (skipChars++; skipChars < expression.length() &&
			Character.isLetter(expression.charAt(skipChars)); skipChars++) {
				numericVariable.append(expression.charAt(skipChars));
			}			

			switch (numericVariable.toString()) {
				case "abs": currentToken = Token.ABS; return;
				case "square": currentToken = Token.SQUARE; return;
				case "mod": currentToken = Token.MOD; return;
			}
			
			switch (numericVariable.toString()) {
				case "x": break;
				case "y": break;
				case "z": break;
				default: throw new UnexpectedVariableNameException(numericVariable.toString(), skipChars);
			}
			
			return;
		}
		
		switch(currentChar) {
			case '+': {
				currentToken = Token.PLUS;
				break;
			}
			
			case '-': {
				currentToken = Token.MINUS;
				break;
			}
			
			case '*': {
				currentToken = Token.MULTIPLY;
				break;
			}
			
			case '/': {
				currentToken = Token.DIVIDE;
				break;
			}
			
			case '(': {
				balance++;
				currentToken = Token.OPEN_BRACKET;
				break;
			}
			
			case ')': {
				balance--;
				if (balance < 0) {
					throw new NotMatchedBracketsException(skipChars + 1);
				}
				
				currentToken = Token.CLOSE_BRACKET;
				break;
			}
		}
		
		if (currentToken == Token.ERROR) {
			throw new UnexpectedSymbolException(currentChar, skipChars + 1);
		}
		
		skipChars++;
	}
	
	private TripleExpression<T> plusAndMinus() throws IllegalExpressionException {
		TripleExpression<T> leftExpression = multiplyAndDivideAndMod();
		
		while (true) {
			switch(currentToken) {
				case PLUS: {
					leftExpression = new CheckedAdd<T>(leftExpression, multiplyAndDivideAndMod());
					break;
				}
				
				case MINUS: {
					leftExpression = new CheckedSubtract<T>(leftExpression, multiplyAndDivideAndMod());
					break;
				}
				
				default: {
					return leftExpression;
				}
			}
		}
	}

	private TripleExpression<T> multiplyAndDivideAndMod() throws IllegalExpressionException {
		TripleExpression<T> leftExpression = bracketsAndVariablesAndUnary();
		
		while (true) {
			switch(currentToken) {
				case MULTIPLY: {
					leftExpression = new CheckedMultiply<T>(leftExpression, bracketsAndVariablesAndUnary());
					break;
				}
				
				case DIVIDE: {				
					leftExpression = new CheckedDivide<T>(leftExpression, bracketsAndVariablesAndUnary());
					break;
				}
				
				case MOD: {
					leftExpression = new Mod<T>(leftExpression, bracketsAndVariablesAndUnary());
					break;
				}
				
				default: {
					return leftExpression;
				}
			}
		}
	}

	private TripleExpression<T> bracketsAndVariablesAndUnary() throws IllegalExpressionException {
		TripleExpression<T> returnExpression = null;
		
		nextToken();
		
		switch(currentToken) {
			case OPEN_BRACKET: {
				returnExpression = plusAndMinus();
				usedUnaryMinus = false;
				
				break;
			}
			
			case CONSTANT: {
				String actuallyNumber = (previousToken == Token.MINUS ? "-" : "") + numericVariable;
				if (!(actuallyNumber).equals(minValueString)) {
					try {
						Integer.valueOf(actuallyNumber);
					} catch (NumberFormatException e) {
						throw new UnexpectedVariableValueException(actuallyNumber, skipChars);
					}
				}
				
				returnExpression = new Const<T>(instance.cast(Integer.parseUnsignedInt(numericVariable.toString())));
				usedUnaryMinus = false;
				
				if (Integer.parseUnsignedInt(numericVariable.toString()) == Integer.MIN_VALUE) {
					if (previousToken != Token.MINUS) {
						throw new UnexpectedVariableValueException(numericVariable.toString(), skipChars);
					}
					
					usedUnaryMinus = true;
				}
				
				break;
			}
			
			case VARIABLE: {
				returnExpression = new Variable<T>(numericVariable.toString());
				usedUnaryMinus = false;
				break;
			}
			
			case MINUS: {
				returnExpression = bracketsAndVariablesAndUnary();
				
				if (!usedUnaryMinus) {
					returnExpression = new CheckedNegate<T>(returnExpression);
				}
				
				usedUnaryMinus = false;
				return returnExpression;
			}
			
			case ABS: {
				usedUnaryMinus = false;
				returnExpression = bracketsAndVariablesAndUnary();
				
				return new Abs<T>(returnExpression);
			}
			
			case SQUARE: {
				usedUnaryMinus = false;
				returnExpression = bracketsAndVariablesAndUnary();
				
				return new Square<T>(returnExpression);
			}
		}

		if (returnExpression == null) {
			throw new ExpressionNotFoundException(currentToken, skipChars);
		}
		
		nextToken();
		
		if (currentToken.isBinaryOperation() && previousToken.isBinaryOperation()) {
			throw new DoubleBinaryOperationsException(currentToken, previousToken);
		}
		
		return returnExpression;
	}
	
	public TripleExpression <T> parse(String expression) throws IllegalExpressionException{
		currentToken = Token.BEGIN;
		previousToken = Token.BEGIN;
		skipChars = 0;
		this.expression = new StringBuilder(expression);
		balance = 0;
		usedUnaryMinus = false;
		numericVariable = new StringBuilder();
		
		TripleExpression<T> returnExpression = plusAndMinus();
		
		if (balance > 0) {
			throw new NotMatchedBracketsException();
		}
		
		return returnExpression;
	}

}
