package expression;

import expression.exceptions.ParserException;

public class High extends UnaryOperation {
	
private final String operationString = "high";
	
	public High(TripleExpression expression) {
		this.expression = expression;
	}

	protected int solve(int x, int y, int z) throws ParserException {
		int expressionValue = expression.evaluate(x, y, z);
		
		return Integer.highestOneBit(expressionValue);
	}

	protected String getOperation() {
		return operationString;
	}
}
