package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerNumber implements Number<Integer> {
	
	private Integer value = 0;
	
	public IntegerNumber(Integer value) {
		this.value = value;
	}
	
	public IntegerNumber() {
		value = 0;
	}
	
	public Number<Integer> add(Number<Integer> a) throws OverflowException{
		if (getValue() < 0 && a.getValue() < 0 && getValue() < Integer.MIN_VALUE - a.getValue()) {
			throw throwOverflow(a);
		}
		if (getValue() > 0 && a.getValue() > 0 && getValue() > Integer.MAX_VALUE - a.getValue()) {
			throw throwOverflow(a);
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue(getValue() + a.getValue());
		return c;
	}

	public Number<Integer> subtract(Number<Integer> a) throws OverflowException {
		if (getValue() < 0 && a.getValue() > 0 && getValue() < Integer.MIN_VALUE + a.getValue()) {
			throw throwOverflow(a);
		}
		
		if (getValue() > 0 && a.getValue() < 0 && getValue() > Integer.MAX_VALUE + a.getValue()) {
			throw throwOverflow(a);
		}
		
		if (getValue() == 0 && a.getValue() == Integer.MIN_VALUE) {
			throw throwOverflow(a);
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue(getValue() - a.getValue());
		return c;
	}

	public Number<Integer> multiply(Number<Integer> a) throws OverflowException {
		if (getValue() > 0 && a.getValue() > 0 && getValue() > Integer.MAX_VALUE / a.getValue()) {
			throw throwOverflow(a);
		}
		
		if (getValue() < 0 && a.getValue() < 0 && getValue() < Integer.MAX_VALUE / a.getValue()) {
			throw throwOverflow(a);
		}
		
		if (getValue() > 0 && a.getValue() < 0 && a.getValue() < Integer.MIN_VALUE / getValue()) {
			throw throwOverflow(a);
		}
		
		if (getValue() < 0 && a.getValue() > 0 && getValue() < Integer.MIN_VALUE / a.getValue()) {
			throw throwOverflow(a);
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue(getValue() * a.getValue());
		return c;
	}

	public Number<Integer> divide(Number<Integer> a) throws OverflowException, DivisionByZeroException {
		if (getValue() == Integer.MIN_VALUE && a.getValue() == -1) {
			throw new OverflowException(getValue() + " + " + a.getValue());
		}
		
		if (a.getValue() == 0) {
			throw new DivisionByZeroException(getValue() + " / " + a.getValue());
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue(getValue() / a.getValue());
		return c;
	}

	public Number<Integer> negate() throws OverflowException {
		if (getValue() == Integer.MIN_VALUE) {
			throw new OverflowException("-" + getValue());
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue(-getValue());
		return c;
	}

	public void setValue(Integer a) {
		value = a;
	}

	public Integer getValue() {
		return value;
	}

	OverflowException throwOverflow(Number<Integer> a) {
		return new OverflowException(getValue() + " + " + a.getValue());
	}

	public Number<Integer> cast(Integer a) {
		Number<Integer> c = new IntegerNumber(a);
		return c;
	}

	public Number<Integer> abs() throws OverflowException {
		if (getValue() == Integer.MIN_VALUE) {
			throw new OverflowException("abs(" + getValue() + ")");
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue(Math.abs(getValue()));
		return c;
	}

	public Number<Integer> square() throws OverflowException {
		if (getValue() == Integer.MIN_VALUE || Math.abs(getValue()) > Math.floor(Math.sqrt(Integer.MAX_VALUE))) {
			throw new OverflowException("square(" + getValue() + ")");
		}
		
		Number<Integer> c = new IntegerNumber();
		c.setValue((int) Math.pow(getValue(), 2));
		return c;
	}

	public Number<Integer> mod(Number<Integer> a) throws OverflowException {
		Number<Integer> c = new IntegerNumber();
		c.setValue(getValue() % a.getValue());
		return c;
	}
}
