package expression;

import expression.exceptions.OverflowException;

public class CheckedAdd extends BinaryOperation {
	
	private final String operationString = "+";
	
	public CheckedAdd(TripleExpression firstOperation, TripleExpression secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}
	
	private void checkOverflowException(int a, int b) throws OverflowException {
		if (a < 0 && b < 0 && a < Integer.MIN_VALUE - b) {
			throw new OverflowException(this);
		}
		if (a > 0 && b > 0 && a > Integer.MAX_VALUE - b) {
			throw new OverflowException(this);
		}
	}
	
	protected int solve(int a, int b) throws OverflowException {
		checkOverflowException(a, b);
		
		return a + b;
	}

	protected String getOperation() {
		return operationString;
	}
}
