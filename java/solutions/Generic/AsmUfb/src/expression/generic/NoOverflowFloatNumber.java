package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class NoOverflowFloatNumber implements Number<Float> {

	private Float value = 0.0F;
	
	public Number<Float> add(Number<Float> a) {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(getValue() + a.getValue());
		return c;
	}

	public Number<Float> subtract(Number<Float> a) {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(getValue() - a.getValue());
		return c;
	}

	public Number<Float> multiply(Number<Float> a) {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(getValue() * a.getValue());
		return c;
	}

	public Number<Float> divide(Number<Float> a) {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(getValue() / a.getValue());
		return c;
	}

	public Number<Float> negate() {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(-getValue());
		return c;
	}

	public void setValue(Float a) {
		value = a;
	}

	public Float getValue() {
		return value;
	}

	public Number<Float> cast(Integer a) {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(a.floatValue());
		return c;
	}

	public Number<Float> abs() {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(Math.abs(getValue()));
		return c;
	}

	public Number<Float> square() {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue((float) Math.pow(getValue(), 2));
		return c;
	}

	public Number<Float> mod(Number<Float> a) {
		Number<Float> c = new NoOverflowFloatNumber();
		c.setValue(getValue() % a.getValue());
		return c;
	}
}
