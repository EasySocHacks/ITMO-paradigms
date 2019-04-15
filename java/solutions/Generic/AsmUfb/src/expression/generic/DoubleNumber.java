package expression.generic;

public class DoubleNumber implements Number<Double> {

	private Double value = 0.0;
	
	public Number<Double> add(Number<Double> a) {
		Number<Double> c = new DoubleNumber();
		c.setValue(getValue() + a.getValue());
		return c;
	}

	public Number<Double> subtract(Number<Double> a) {
		Number<Double> c = new DoubleNumber();
		c.setValue(getValue() - a.getValue());
		return c;
	}

	public Number<Double> multiply(Number<Double> a) {
		Number<Double> c = new DoubleNumber();
		c.setValue(getValue() * a.getValue());
		return c;
	}

	public Number<Double> divide(Number<Double> a) {
		Number<Double> c = new DoubleNumber();
		c.setValue(getValue() / a.getValue());
		return c;
	}

	public Number<Double> negate() {
		Number<Double> c = new DoubleNumber();
		c.setValue(-getValue());
		return c;
	}

	public void setValue(Double a) {
		value = a;
	}

	public Double getValue() {
		return value;
	}

	public Number<Double> cast(Integer a) {
		Number<Double> c = new DoubleNumber();
		c.setValue(a.doubleValue());
		return c;
	}

	public Number<Double> abs() {
		Number<Double> c = new DoubleNumber();
		c.setValue(Math.abs(getValue()));
		return c;
	}

	public Number<Double> square(){
		Number<Double> c = new DoubleNumber();
		c.setValue(Math.pow(getValue(), 2));
		return c;
	}

	public Number<Double> mod(Number<Double> a) {
		Number<Double> c = new DoubleNumber();
		c.setValue(getValue() % a.getValue());
		return c;
	}
}
