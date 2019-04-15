package expression.generic;

import java.math.BigInteger;

import expression.exceptions.DivisionByZeroException;

public class BigIntegerNumber implements Number<BigInteger> {

	private BigInteger value = new BigInteger("0");
	
	public Number<BigInteger> add(Number<BigInteger> a) {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().add(a.getValue()));
		return c;
	}

	public Number<BigInteger> subtract(Number<BigInteger> a) {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().subtract(a.getValue()));
		return c;
	}

	public Number<BigInteger> multiply(Number<BigInteger> a) {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().multiply(a.getValue()));
		return c;
	}

	public Number<BigInteger> divide(Number<BigInteger> a) throws DivisionByZeroException {
		if (a.getValue() == BigInteger.ZERO) {
			throw new DivisionByZeroException(getValue() + "/" + a.getValue());
		}
		
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().divide(a.getValue()));
		return c;
	}

	public Number<BigInteger> negate() {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().negate());
		return c;
	}

	public void setValue(BigInteger a) {
		value = a;
	}

	public BigInteger getValue() {
		return value;
	}

	public Number<BigInteger> cast(Integer a) {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(new BigInteger(a.toString()));
		return c;
	}

	public Number<BigInteger> abs() {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().abs());
		return c;
	}

	public Number<BigInteger> square() {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().multiply(getValue()));
		return c;
	}

	public Number<BigInteger> mod(Number<BigInteger> a) {
		Number<BigInteger> c = new BigIntegerNumber();
		c.setValue(getValue().mod(a.getValue()));
		return c;
	}
}
