package expression;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ParserException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends BinaryOperation {
	
	private final String operationString = "/";
	
	public CheckedDivide(TripleExpression firstOperation, TripleExpression secondOperation) {
		this.firstOperation = firstOperation;
		this.secondOperation = secondOperation;
	}
	
	private void checkOverflowException(int a, int b) throws OverflowException {
		if (a == Integer.MIN_VALUE && b == -1) {
			throw new OverflowException(this);
		}
	}
	
	private void checkDivisionByZeroException(int b) throws DivisionByZeroException {
		if (b == 0) {
			throw new DivisionByZeroException(this);
		}
	}
	
	protected int solve(int a, int b) throws ParserException{
		checkDivisionByZeroException(b);
		checkOverflowException(a, b);
		
		return a / b;
	}

	protected String getOperation() {
		return operationString;
	}
}
