package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public class CheckedMultiply<T> extends BinaryOperation<T> {

	private final String operationString = "*";
	
	public CheckedMultiply(TripleExpression<T> firstOperation, TripleExpression<T> secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}
	
	protected Number<T> solve(Number<T> a, Number<T> b) throws ParserException {		
		return a.multiply(b);
	}

	protected String getOperation() {
		return operationString;
	}
}
