package br.cin.ufpe.nlp.tokenizers;

import java.io.IOException;

import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import br.cin.ufpe.nlp.api.tokenization.TokenizerFactory;
import br.cin.ufpe.nlp.util.AnnotatedToken;

public class MultipleAnnotationTokenizerFactory extends BaseTokenizerFactory<Tokenizer<AnnotatedToken>>
implements TokenizerFactory<Tokenizer<AnnotatedToken>>
{

	public MultipleAnnotationTokenizerFactory() {
	}

	@Override
	public Tokenizer<AnnotatedToken> createFromText(String text) throws IOException {
		return new MultipleAnnotationTokenizer(text);
	}
	
	public static void main(String[] args) throws IOException {
        String text = "i was  dancing the polka in january,do you dance the polka ? I [do] think so. please visit https://www.facebook.com/sergio.queiroz and http://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java. Hello? Are you there?"; // Add your text here!
        MultipleAnnotationTokenizerFactory multiFactory = new MultipleAnnotationTokenizerFactory();
        Tokenizer<AnnotatedToken> multiTokenizer = multiFactory.createFromText(text);
        while(multiTokenizer.hasMoreTokens()) {
        	AnnotatedToken token = multiTokenizer.nextToken();
        	System.out.println(token);
        }
	}

}
