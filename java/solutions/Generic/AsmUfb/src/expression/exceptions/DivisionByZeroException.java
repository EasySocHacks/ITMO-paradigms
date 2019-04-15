package expression.exceptions;

import expression.TripleExpression;

public class DivisionByZeroException extends ParserException {
	public DivisionByZeroException(TripleExpression expression) {
		super("Division by zero in expression\n" + expression.toString());
	}
	
	public DivisionByZeroException(String expression) {
		super("Division by zero in expression\n" + expression);
	}
}
