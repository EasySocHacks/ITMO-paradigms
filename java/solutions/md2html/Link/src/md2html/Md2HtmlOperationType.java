package md2html;

public enum Md2HtmlOperationType {
	NONE(""),
	SELECTION_STAR("*"),
	SELECTION_UNDERLINE("_"),
	STRONG_SELECTION_STAR("**"),
	STRONG_SELECTION_UNDERLINE("__"),
	STRIKETHROUGH("--"),
	CODE("`"),
	LINK_INPUT("["),
	LINK_HREF("(");
	
	private String operation;
	
	private Md2HtmlOperationType(String operation) {
		this.operation = operation;
	}
	
	public String getOperation() {
		return operation;
	}
}
