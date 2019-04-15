package expression.exceptions;

public class UnexpectedSymbolException extends IllegalExpressionException {

	public UnexpectedSymbolException(char symbol, int pos) {
		super("Unexpected symbol \'" + symbol + "\' in expression at position " + pos);
	}
	
}
