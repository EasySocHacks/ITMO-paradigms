package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;
public class CheckedNegate<T> extends UnaryOperation<T> {
	
	private final String operationString = "-";
	
	public CheckedNegate(TripleExpression<T> expression) {
		this.expression = expression;
	}
	
	protected Number<T> solve(Number<T> x, Number<T> y, Number<T> z) throws ParserException {
		Number<T> value = expression.evaluate(x, y, z);
				
		return value.negate();
	}

	protected String getOperation() {
		return operationString;
	}

}
