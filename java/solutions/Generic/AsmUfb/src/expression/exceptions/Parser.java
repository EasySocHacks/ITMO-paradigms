package expression.exceptions;

import expression.TripleExpression;
import expression.generic.Number;

public interface Parser<T> {
   TripleExpression<T> parse(String expression) throws ParserException;
}