package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public class CheckedDivide<T> extends BinaryOperation<T> {
	
	private final String operationString = "/";
	
	public CheckedDivide(TripleExpression<T> firstOperation, TripleExpression<T> secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}
	
	protected Number<T> solve(Number<T> a, Number<T> b) throws ParserException{
		return a.divide(b);
	}

	protected String getOperation() {
		return operationString;
	}
}
