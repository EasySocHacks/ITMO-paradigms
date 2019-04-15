package expression.generic;

import expression.exceptions.DivisionByZeroException;

public class NoOverflowIntegerNumber implements Number<Integer> {

	private Integer value = 0;
	
	public Number<Integer> add(Number<Integer> a) {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(getValue() + a.getValue());
		return c;
	}

	public Number<Integer> subtract(Number<Integer> a) {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(getValue() - a.getValue());
		return c;
	}

	public Number<Integer> multiply(Number<Integer> a) {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(getValue() * a.getValue());
		return c;
	}

	public Number<Integer> divide(Number<Integer> a) throws DivisionByZeroException {
		if (a.getValue() == 0) {
			throw new DivisionByZeroException(getValue() + "/" + a.getValue());
		}
		
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(getValue() / a.getValue());
		return c;
	}

	public Number<Integer> negate() {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(-getValue());
		return c;
	}

	public void setValue(Integer a) {
		value = a;
	}

	public Integer getValue() {
		return value;
	}

	public Number<Integer> cast(Integer a) {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(a);
		return c;
	}

	public Number<Integer> abs() {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(Math.abs(getValue()));
		return c;
	}

	public Number<Integer> square() {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(getValue() * getValue());
		return c;
	}

	public Number<Integer> mod(Number<Integer> a) {
		Number<Integer> c = new NoOverflowIntegerNumber();
		c.setValue(getValue() % a.getValue());
		return c;
	}
}
