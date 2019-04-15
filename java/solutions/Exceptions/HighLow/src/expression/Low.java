package expression;

import expression.exceptions.ParserException;

public class Low extends UnaryOperation {

	private final String operationString = "low";
	
	public Low(TripleExpression expression) {
		this.expression = expression;
	}
	
	protected int solve(int x, int y, int z) throws ParserException {
		int expressionValue = expression.evaluate(x, y, z);
		
		return Integer.lowestOneBit(expressionValue);
	}

	protected String getOperation() {
		return operationString;
	}

}
