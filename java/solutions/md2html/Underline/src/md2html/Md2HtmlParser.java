package md2html;

public class Md2HtmlParser {

	private final Md2HtmlSource source;
	private boolean endOfParagraph = false;
		
	public Md2HtmlParser(Md2HtmlSource source) throws Md2HtmlException {
		this.source = source;
		source.nextChar();
	}
	
	public StringBuilder parse() throws Md2HtmlException {
		StringBuilder html = new StringBuilder();
		skipLineSeparators();

		while (source.getChar() != source.END) {
			html.append(parseParagraph());
			html.append(System.lineSeparator());
			skipLineSeparators();
		}
		
		return html;
	}
	
	private StringBuilder parseParagraph() throws Md2HtmlException {
		StringBuilder paragraph = new StringBuilder();
		String header = "";
		
		StringBuilder possibleHeader = parseHeaders();
		
		switch (possibleHeader.toString()) {
			case "#": header = "1"; break;
			case "##": header = "2"; break;
			case "###": header = "3"; break;
			case "####": header = "4"; break;
			case "#####": header = "5"; break;
			case "######": header = "6"; break;
		}
	
		if (!header.isEmpty() && Character.isWhitespace(source.getChar())) {
			source.nextChar();
		} else {
			header = "";
		}
		
		if (!header.isEmpty()) {
			paragraph.append("<h" + header + ">");
			header = "</h" + header + ">";
		} else {
			paragraph.append("<p>" + possibleHeader);
			header = "</p>";
		}
		
		paragraph.append(parseText(Md2HtmlOperationType.NONE));
		
		while (paragraph.charAt(paragraph.length() - 1) == '\r' || paragraph.charAt(paragraph.length() - 1) == '\n') {
			paragraph.deleteCharAt(paragraph.length() - 1);
		}
		
		endOfParagraph = false;
		paragraph.append(header);
		
		return paragraph;
	}
	
	private StringBuilder parseText(Md2HtmlOperationType expectedOperation) throws Md2HtmlException {
		StringBuilder text = new StringBuilder();
		int lineSeparatorsCounter = 0;
		
		while (source.getChar() != source.END) {
			if (endOfParagraph) {
				break;
			}
			
			if (isLineSeparator()) {
				lineSeparatorsCounter++;
				text.append(System.lineSeparator());
				source.nextChar();
				
				if (lineSeparatorsCounter == 2) {
					break;
				}
				
				continue;
			} else {
				lineSeparatorsCounter = 0;
			}
			
			if (source.getChar() == '<') {
				source.nextChar();
				text.append("&lt;");
				continue;
			}
			
			if (source.getChar() == '>') {
				source.nextChar();
				text.append("&gt;");
				continue;
			}
			
			if (source.getChar() == '&') {
				source.nextChar();
				text.append("&amp;");
				continue;
			}
			
			if (source.getChar() == '\\') {
				text.append(source.nextChar());
				source.nextChar();
				continue;
			}
			
			if (source.getChar() == '*') {
				if (source.nextChar() == '*') {
					source.nextChar();
					if (expectedOperation == Md2HtmlOperationType.STRONG_SELECTION_STAR) {
						text = new StringBuilder("<strong>" + text + "</strong>");
						return text;
					}
					
					text.append(parseText(Md2HtmlOperationType.STRONG_SELECTION_STAR));
				} else {
					if (expectedOperation == Md2HtmlOperationType.SELECTION_STAR) {
						text = new StringBuilder("<em>" + text + "</em>");
						return text;
					}
					
					text.append(parseText(Md2HtmlOperationType.SELECTION_STAR));
				}
			} else if (source.getChar() == '_') {
				if (source.nextChar() == '_') {
					source.nextChar();
					if (expectedOperation == Md2HtmlOperationType.STRONG_SELECTION_UNDERLINE) {
						text = new StringBuilder("<strong>" + text + "</strong>");
						return text;
					}
					
					text.append(parseText(Md2HtmlOperationType.STRONG_SELECTION_UNDERLINE));
				} else {
					if (expectedOperation == Md2HtmlOperationType.SELECTION_UNDERLINE) {
						text = new StringBuilder("<em>" + text + "</em>");
						return text;
					}
					
					text.append(parseText(expectedOperation.SELECTION_UNDERLINE));
				}
			} else if (source.getChar() == '-') {
				if (source.nextChar() == '-') {
					source.nextChar();
					if (expectedOperation == Md2HtmlOperationType.STRIKETHROUGH) {
						text = new StringBuilder("<s>" + text + "</s>");
						return text;
					}
					
					text.append(parseText(expectedOperation.STRIKETHROUGH));
				} else {
					text.append('-');
				}
			} else if (source.getChar() == '`') {
				source.nextChar();
				if (expectedOperation == Md2HtmlOperationType.CODE) {
					text = new StringBuilder("<code>" + text + "</code>");
					return text;
				}
				
				text.append(parseText(expectedOperation.CODE));
			} else if (source.getChar() == '+') {
				if (source.nextChar() == '+') {
					source.nextChar();
					if (expectedOperation == Md2HtmlOperationType.UNDERLINE) {
						text = new StringBuilder("<u>" + text + "</u>");
						return text;
					}
					
					text.append(parseText(expectedOperation.UNDERLINE));
				} else {
					text.append("+");
				}
			} else {
				text.append(source.getChar());
				source.nextChar();
			}
		}
		
		endOfParagraph = true;
		
		if (expectedOperation != Md2HtmlOperationType.NONE) {
			return new StringBuilder(expectedOperation.getOperation() + text);
		}
		
		return text;
	}
	
	private StringBuilder parseHeaders() throws Md2HtmlException {
		StringBuilder header = new StringBuilder();
		
		while (source.getChar() == '#') {
			header.append('#');
			source.nextChar();
		}
		
		return header;
	}
	
	private void skipLineSeparators() throws Md2HtmlException {
		while (isLineSeparator()) {
			source.nextChar();
		}
	}
	
	private boolean isLineSeparator() throws Md2HtmlException {
		if (source.getChar() == '\r') {
			source.nextChar();
			return true;
		} else if (source.getChar() == '\n') {
			return true;
		} else {
			return false;
		}
	}
}
