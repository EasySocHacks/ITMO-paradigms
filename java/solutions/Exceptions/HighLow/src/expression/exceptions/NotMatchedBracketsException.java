package expression.exceptions;


public class NotMatchedBracketsException extends IllegalExpressionException {

	public NotMatchedBracketsException(int pos) {
		super("Not matched brackets contains in expression (found balance < 0) at position " + pos);
	}

	public NotMatchedBracketsException() {
		super("Not matched brackets contains in expression (expression balance > 0)");
	}
}
