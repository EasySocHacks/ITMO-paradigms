package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public abstract class UnaryOperation<T> implements TripleExpression<T> {
	
	TripleExpression<T> expression;
	
	protected abstract Number<T> solve(Number<T> x, Number<T> y, Number<T> z) throws ParserException ;
	protected abstract String getOperation();
	
	public Number<T> evaluate(Number<T> x, Number<T> y, Number<T> z) throws ParserException {
		return solve(x, y, z);
	}
	
	public String toString() {
		return getOperation() + expression.toString();
	}
}
