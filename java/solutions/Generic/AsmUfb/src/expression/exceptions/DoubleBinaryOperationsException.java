package expression.exceptions;

import expression.parser.Token;

public class DoubleBinaryOperationsException extends IllegalExpressionException {

	public DoubleBinaryOperationsException(Token firstToken, Token secondToken) {
		super("Unexpected double binary operations \"" + firstToken.getTokenString() + secondToken.getTokenString() + "\"");
	}
	
}
