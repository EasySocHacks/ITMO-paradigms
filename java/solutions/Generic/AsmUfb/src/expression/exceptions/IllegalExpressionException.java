package expression.exceptions;

public class IllegalExpressionException extends ParserException {
	public IllegalExpressionException(String message) {
		super("[Illegal expression]: " + message);
	}
}
