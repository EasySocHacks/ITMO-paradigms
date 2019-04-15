package expression.generic;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public interface Number<T> {
	
	public Number<T> add(Number<T> a) throws OverflowException;
	public Number<T> subtract(Number<T> a) throws OverflowException;
	public Number<T> multiply(Number<T> a) throws OverflowException;
	public Number<T> divide(Number<T> a) throws OverflowException, DivisionByZeroException;
	public Number<T> negate() throws OverflowException;
	public Number<T> abs() throws OverflowException;
	public Number<T> square() throws OverflowException;
	public Number<T> mod(Number<T> a) throws OverflowException;
	
	public void setValue(T a);
	public T getValue();
	
	public Number<T> cast(Integer a);
}
