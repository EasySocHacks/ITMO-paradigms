package expression.parser;

public enum Token {	
	BEGIN("", false),
	END("", false),
	MULTIPLY("*", true),
	DIVIDE("/", true),
	PLUS("+", true),
	MINUS("-", true),
	OPEN_BRACKET("(", false),
	CLOSE_BRACKET(")", false),
	ERROR("", false),
	VARIABLE("", false),
	CONSTANT("", false),
	HIGH("", true),
	LOW("", true);
	
	private String tokenString;
	private boolean isBinaryOperation;
	
	private Token(String tokenString, boolean isBinaryOperation) {
		this.tokenString = tokenString;
		this.isBinaryOperation = isBinaryOperation;
	}
	
	public String getTokenString() {
		return tokenString;
	}
	
	public boolean isBinaryOperation() {
		return isBinaryOperation;
	}
}
