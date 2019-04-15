package expression;

import expression.generic.Number;

public abstract class NumericValue<T> implements TripleExpression<T> {	
	abstract protected Number<T> getValue(Number<T> x, Number<T> y, Number<T> z);
	abstract protected String getValue(); 
	
	public Number<T> evaluate(Number<T> x, Number<T> y, Number<T> z) {
		return getValue(x, y, z);
	}
	
	public String toString() {
		return getValue();
	}
}
