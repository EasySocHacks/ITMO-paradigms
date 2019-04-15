package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public class Square<T> extends UnaryOperation<T> {

	private final String operationString = "square";
	
	public Square(TripleExpression<T> expression) {
		this.expression = expression;
	}
	
	protected Number<T> solve(Number<T> x, Number<T> y, Number<T> z) throws ParserException {
		Number<T> value = expression.evaluate(x, y, z);
		
		return value.square();
	}

	protected String getOperation() {
		return operationString;
	}
}
