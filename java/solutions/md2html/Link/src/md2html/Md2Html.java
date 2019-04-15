package md2html;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Md2Html {

	public static void main(String args[]) {
		String inputFile = args[0];
		String outputFile = args[1];
		try {
			PrintWriter writer = new PrintWriter(new File(outputFile), StandardCharsets.UTF_8);
			FileMd2HtmlSource source = new FileMd2HtmlSource(inputFile);
			Md2HtmlParser parser = new Md2HtmlParser(source);
			
			StringBuilder htmlText = parser.parse();
			
			source.closeReader();
			
			writer.write(htmlText.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Md2HtmlException e) {
			e.printStackTrace();
		}
	}
}
