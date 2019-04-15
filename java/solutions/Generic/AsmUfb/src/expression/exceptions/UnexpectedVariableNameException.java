package expression.exceptions;

public class UnexpectedVariableNameException extends IllegalExpressionException {

	public UnexpectedVariableNameException(String variableName, int pos) {
		super("Unexpected variable name \"" + variableName + "\" at position " + pos);
	}

}
