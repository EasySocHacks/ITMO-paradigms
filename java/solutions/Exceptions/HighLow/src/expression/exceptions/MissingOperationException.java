package expression.exceptions;

import expression.parser.Token;

public class MissingOperationException extends IllegalExpressionException {

	public MissingOperationException(Token firstToken, Token secondToken) {
		super("Missing operation between \'" + firstToken.getTokenString() + "\' " +
		"and \'" + secondToken.getTokenString() + "\'");
	}

}
