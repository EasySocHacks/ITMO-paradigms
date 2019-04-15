package md2html;

import java.io.IOException;

public abstract class Md2HtmlSource {
	public static final char END = '\0';
	
	protected int pos = 0;
	protected int line = 1;
	protected int posInLine = 0;
	private char previousChar = END;
	private char currentChar;
	
	protected abstract char readChar() throws IOException;
	
	public char getChar() {
		return currentChar;
	}
	
	public char getPreviousChar() {
		return previousChar;
	}
	
	public char nextChar() throws Md2HtmlException {
		try {
			if (currentChar == Character.LINE_SEPARATOR) {
				line++;
				posInLine = 0;
			}
			
			previousChar = currentChar;
			currentChar = readChar();
			pos++;
			posInLine++;
			return currentChar;
		} catch(final IOException e) {
			throw error("Source read error: %s", e.getMessage());
		}
	}
	
	public Md2HtmlException error(final String format, final Object... args) throws Md2HtmlException {
        return new Md2HtmlException(line, posInLine, String.format("%d:%d: %s", line, posInLine, String.format(format, args)));
    }
}
