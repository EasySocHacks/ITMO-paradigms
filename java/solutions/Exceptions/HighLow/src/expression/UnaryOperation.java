package expression;

import expression.exceptions.ParserException;

public abstract class UnaryOperation implements TripleExpression {
	
	TripleExpression expression;
	
	protected abstract int solve(int x, int y, int z) throws ParserException ;
	protected abstract String getOperation();
	
	public int evaluate(int x, int y, int z) throws ParserException {
		return solve(x, y, z);
	}
	
	public String toString() {
		return getOperation() + expression.toString();
	}
}
