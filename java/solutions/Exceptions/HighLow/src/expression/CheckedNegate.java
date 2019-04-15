package expression;

import expression.exceptions.ParserException;
import expression.exceptions.OverflowException;
public class CheckedNegate extends UnaryOperation {
	
	private final String operationString = "-";
	
	public CheckedNegate(TripleExpression expression) {
		this.expression = expression;
	}
	
	private void checkOverflowException(int value) throws OverflowException {
		if (value == Integer.MIN_VALUE) {
			throw new OverflowException(this);
		}
	}
	
	protected int solve(int x, int y, int z) throws ParserException {
		int value = expression.evaluate(x, y, z);
		
		checkOverflowException(value);
		
		return -value;
	}

	protected String getOperation() {
		return operationString;
	}

}
