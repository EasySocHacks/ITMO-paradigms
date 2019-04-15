package md2html;

public class Md2HtmlException extends Throwable {
	private int pos;
	private int line;
	
	public Md2HtmlException(final int pos, final int line, final String message) {
		super(message);
		this.pos = pos;
		this.line = line;
	}
	
	public int getPosition() {
		return pos;
	}
	
	public int getLine() {
		return line;
	}
}
