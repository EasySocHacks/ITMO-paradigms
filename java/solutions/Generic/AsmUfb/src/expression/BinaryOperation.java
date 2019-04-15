package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public abstract class BinaryOperation<T> implements TripleExpression<T> {
	protected TripleExpression<T> firstOperation;
	protected TripleExpression<T> secondOperation;
	
	protected abstract Number<T> solve(Number<T> a, Number<T> b) throws ParserException;
	protected abstract String getOperation();
	
	public Number<T> evaluate(Number<T> x, Number<T> y, Number<T> z) throws ParserException {
		return solve(firstOperation.evaluate(x, y, z), secondOperation.evaluate(x, y, z));
	}
	
	public String toString() {
		return "(" + firstOperation.toString() + getOperation() + secondOperation.toString() + ")";
	}
}
