package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public class Const<T> implements TripleExpression<T> {
	Number<T> value;
	
	public Const(Number<T> value) {
		this.value = value;
	}
	
	public Const(T value) {
		this.value.setValue(value);
	}

	public Number<T> evaluate(Number<T> x, Number<T> y, Number<T> z) throws ParserException {
		return value;
	}
	
	public Number<T> getConstValue() {
		return value;
	}

	protected String getValue() {
		return String.valueOf(value.getValue());
	}
}
