package expression;

import expression.exceptions.ParserException;
import expression.exceptions.OverflowException;

public class CheckedSubtract extends BinaryOperation {
	
	private final String operationString = "-";
	
	public CheckedSubtract(TripleExpression firstOperation, TripleExpression secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}
	
	private void checkOverflowException(int a, int b) throws OverflowException {
		if (a < 0 && b > 0 && a < Integer.MIN_VALUE + b) {
			throw new OverflowException(this);
		}
		
		if (a > 0 && b < 0 && a > Integer.MAX_VALUE + b) {
			throw new OverflowException(this);
		}
		
		if (a == 0 && b == Integer.MIN_VALUE) {
			throw new OverflowException(this);
		}
	}

	protected int solve(int a, int b) throws ParserException {
		checkOverflowException(a, b);
		
		return a - b;
	}

	protected String getOperation() {
		return operationString;
	}
}
