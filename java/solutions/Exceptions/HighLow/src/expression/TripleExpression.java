package expression;

import expression.exceptions.ParserException;

public interface TripleExpression {
    int evaluate(int x, int y, int z) throws ParserException;
    
    String toString();
}
