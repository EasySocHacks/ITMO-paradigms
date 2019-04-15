package expression.generic;

import expression.exceptions.DivisionByZeroException;

public class NoOverflowByteNumber implements Number<Byte> {

	private Byte value = 0;
	
	public Number<Byte> add(Number<Byte> a) {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) (getValue() + a.getValue()));
		return c;
	}

	public Number<Byte> subtract(Number<Byte> a) {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) (getValue() - a.getValue()));
		return c;
	}

	public Number<Byte> multiply(Number<Byte> a) {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) (getValue() * a.getValue()));
		return c;
	}

	public Number<Byte> divide(Number<Byte> a) throws DivisionByZeroException {
		if (a.getValue() == 0) {
			throw new DivisionByZeroException(getValue() + "/" + a.getValue());
		}
		
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) (getValue() / a.getValue()));
		return c;
	}

	public Number<Byte> negate() {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) -getValue());
		return c;
	}

	public void setValue(Byte a) {
		value = a;
	}

	public Byte getValue() {
		return value;
	}

	public Number<Byte> cast(Integer a) {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue(a.byteValue());
		return c;
	}

	public Number<Byte> abs() {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) Math.abs(getValue()));
		return c;
	}

	public Number<Byte> square() {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) Math.pow(getValue(), 2));
		return c;
	}

	public Number<Byte> mod(Number<Byte> a) {
		Number<Byte> c = new NoOverflowByteNumber();
		c.setValue((byte) (getValue() % a.getValue()));
		return c;
	}
}
