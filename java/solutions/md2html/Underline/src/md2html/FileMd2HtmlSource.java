package md2html;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class FileMd2HtmlSource extends Md2HtmlSource {

	private final Reader reader;
	
	public FileMd2HtmlSource(final String fileName) throws Md2HtmlException {
		try {
			reader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
		} catch(final IOException e) {
			throw error("Error occurred while opening file '%s': %s", fileName, e.getMessage());
		}
	}

	protected char readChar() throws IOException {
		final int read = reader.read();
		return read == -1 ? END : (char)read;
	}

	public void closeReader() throws IOException {
		reader.close();
	}
}
