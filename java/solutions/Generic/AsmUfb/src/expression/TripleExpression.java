package expression;

import expression.exceptions.ParserException;
import expression.generic.Number;

public interface TripleExpression<T> {
	Number<T> evaluate(Number<T> x, Number<T> y, Number<T> z) throws ParserException;
    
    String toString();
}
