package expression.parser;

import expression.CheckedAdd;
import expression.Const;
import expression.High;
import expression.Low;
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

public class ExpressionParser implements Parser{
	private Token currentToken;
	private Token previousToken;
	private int skipChars;
	private StringBuilder expression;
	private StringBuilder numericVariable;
	private int balance;
	private final String minValueString = String.valueOf(Integer.MIN_VALUE);
	private boolean usedUnaryMinus;
	
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
			
			if (numericVariable.toString().equals("high") || numericVariable.toString().equals("low")) {
				switch (numericVariable.toString()) {
					case "high": currentToken = Token.HIGH; break;
					case "low": currentToken = Token.LOW; break;
				}
				
				return;
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
	
	private TripleExpression plusAndMinus() throws IllegalExpressionException {
		TripleExpression leftExpression = multiplyAndDivide();
		
		while (true) {
			switch(currentToken) {
				case PLUS: {
					leftExpression = new CheckedAdd(leftExpression, multiplyAndDivide());
					break;
				}
				
				case MINUS: {
					leftExpression = new CheckedSubtract(leftExpression, multiplyAndDivide());
					break;
				}
				
				default: {
					return leftExpression;
				}
			}
		}
	}

	private TripleExpression multiplyAndDivide() throws IllegalExpressionException {
		TripleExpression leftExpression = bracketsAndVariablesAndUnary();
		
		while (true) {
			switch(currentToken) {
				case MULTIPLY: {
					leftExpression = new CheckedMultiply(leftExpression, bracketsAndVariablesAndUnary());
					break;
				}
				
				case DIVIDE: {				
					leftExpression = new CheckedDivide(leftExpression, bracketsAndVariablesAndUnary());
					break;
				}
				
				default: {
					return leftExpression;
				}
			}
		}
	}

	private TripleExpression bracketsAndVariablesAndUnary() throws IllegalExpressionException {
		TripleExpression returnExpression = null;
		
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
				
				returnExpression = new Const(Integer.parseUnsignedInt(numericVariable.toString()));
				usedUnaryMinus = false;
				
				if (((Const)returnExpression).getConstValue() == Integer.MIN_VALUE) {
					if (previousToken != Token.MINUS) {
						throw new UnexpectedVariableValueException(numericVariable.toString(), skipChars);
					}
					
					usedUnaryMinus = true;
				}
				
				break;
			}
			
			case VARIABLE: {
				returnExpression = new Variable(numericVariable.toString());
				usedUnaryMinus = false;
				break;
			}
			
			case MINUS: {
				returnExpression = bracketsAndVariablesAndUnary();
				
				if (!usedUnaryMinus) {
					returnExpression = new CheckedNegate(returnExpression);
				}
				
				usedUnaryMinus = false;
				return returnExpression;
			}
			
			case HIGH: {
				returnExpression = bracketsAndVariablesAndUnary();
				returnExpression = new High(returnExpression);
				
				usedUnaryMinus = false;
				return returnExpression;
			}
			
			case LOW: {
				returnExpression = bracketsAndVariablesAndUnary();
				returnExpression = new Low(returnExpression);
				
				usedUnaryMinus = false;
				return returnExpression;
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
	
	public TripleExpression parse(String expression) throws IllegalExpressionException{
		currentToken = Token.BEGIN;
		previousToken = Token.BEGIN;
		skipChars = 0;
		this.expression = new StringBuilder(expression);
		balance = 0;
		usedUnaryMinus = false;
		numericVariable = new StringBuilder();
		
		TripleExpression returnExpression = plusAndMinus();
		
		if (balance > 0) {
			throw new NotMatchedBracketsException();
		}
		
		return returnExpression;
	}

}
