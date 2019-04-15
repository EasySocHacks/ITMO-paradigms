package expression.exceptions;

import expression.TripleExpression;

public class OverflowException extends ParserException {
	public OverflowException(TripleExpression expression) {
		super("Overflow integer [" + Integer.MIN_VALUE + "; " + Integer.MAX_VALUE + "] in expression\n" +
		expression.toString());
	}
	
	public OverflowException(String expression) {
		super("Overflow integer [" + Integer.MIN_VALUE + "; " + Integer.MAX_VALUE + "] in expression\n" +
		expression);
	}
}
