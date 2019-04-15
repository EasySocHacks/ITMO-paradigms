package expression.exceptions;

import expression.parser.Token;

public class ExpressionNotFoundException extends IllegalExpressionException {

	public ExpressionNotFoundException(Token token, int pos) {
		super("Expression not fount before \'" + token.getTokenString() + "\' at position " + pos);
	}

}
