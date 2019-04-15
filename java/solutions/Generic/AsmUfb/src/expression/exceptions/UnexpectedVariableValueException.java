package expression.exceptions;

public class UnexpectedVariableValueException extends IllegalExpressionException {

	public UnexpectedVariableValueException(String value, int pos) {
		super("Unexpected variable value \"" + value + "\" at position " + pos);
	}
}
