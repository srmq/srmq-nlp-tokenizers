package br.cin.ufpe.nlp.tokenizers;

import java.io.IOException;

public class LemmaTokenizerFactory extends BaseTokenizerFactory<LemmaTokenizer>{

	public LemmaTokenizerFactory() {
	}

	@Override
	public LemmaTokenizer createFromText(String text) throws IOException {
		return new LemmaTokenizer(text);
	}
	
	public static void main(String[] args) throws IOException {
        String text = "i was  dancing the polka ,do you dance the polka ? I [do] think so. please visit https://www.facebook.com/sergio.queiroz and http://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java. Hello? Are you there?"; // Add your text here!
        LemmaTokenizerFactory lemmaFactory = new LemmaTokenizerFactory();
        LemmaTokenizer lemmaTokenizer = lemmaFactory.createFromText(text);
        while(lemmaTokenizer.hasMoreTokens()) {
        	String lemma = lemmaTokenizer.nextToken();
        	System.out.println(lemma);
        }
	}

}
