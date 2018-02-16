package br.cin.ufpe.nlp.tokenizers;

import java.io.IOException;

public class LineTokenizerFactory extends BaseTokenizerFactory<LineTokenizer>{

	public LineTokenizerFactory() {
	}

	@Override
	public LineTokenizer createFromText(String text) throws IOException {
		return new LineTokenizer(text);
	}
	
	public static void main(String[] args) throws IOException {
        String text = "i was  dancing the polka ,\ndo you dance the polka ?\n I [do] think so.\n please visit https://www.facebook.com/sergio.queiroz and http://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java. \nHello? \nAre you there?"; // Add your text here!
        LineTokenizerFactory lineFactory = new LineTokenizerFactory();
        LineTokenizer lineTokenizer = lineFactory.createFromText(text);
        while(lineTokenizer.hasMoreTokens()) {
        	String line = lineTokenizer.nextToken();
        	System.out.println(line);
        }
	}

}
