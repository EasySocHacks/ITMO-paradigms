package expression;

import expression.exceptions.ParserException;

public abstract class BinaryOperation implements TripleExpression {
	protected TripleExpression firstOperation;
	protected TripleExpression secondOperation;
	
	protected abstract int solve(int a, int b) throws ParserException;
	protected abstract String getOperation();
	
	public int evaluate(int x, int y, int z) throws ParserException {
		return solve(firstOperation.evaluate(x, y, z), secondOperation.evaluate(x, y, z));
	}
	
	public String toString() {
		return "(" + firstOperation.toString() + getOperation() + secondOperation.toString() + ")";
	}
}
