package expression;

import expression.generic.Number;

public class Variable<T> implements TripleExpression<T> {
	private String name;
	
	public Variable(String name) {
		this.name = name;
	}
	
	public Number<T> evaluate(Number<T> x, Number<T> y, Number<T> z) {
		if (name.equals("x")) {
			return x;
		} else if (name.equals("y")) {
			return y;
		} else {
			return z;
		}
	}

	protected String getValue() {
		return name;
	}
}
