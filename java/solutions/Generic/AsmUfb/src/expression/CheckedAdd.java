package expression;

import expression.exceptions.OverflowException;
import expression.generic.Number;

public class CheckedAdd<T> extends BinaryOperation<T> {
	
	private final String operationString = "+";
	
	public CheckedAdd(TripleExpression<T> firstOperation, TripleExpression<T> secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}
	
	protected Number<T> solve(Number<T> a, Number<T> b) throws OverflowException {
		return a.add(b);
	}

	protected String getOperation() {
		return operationString;
	}
}
