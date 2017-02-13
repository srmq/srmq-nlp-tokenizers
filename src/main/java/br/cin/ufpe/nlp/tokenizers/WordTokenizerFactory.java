package br.cin.ufpe.nlp.tokenizers;

import java.io.IOException;

public class WordTokenizerFactory extends BaseTokenizerFactory<WordTokenizer>{

	public WordTokenizerFactory() {
	}

	@Override
	public WordTokenizer createFromText(String text) throws IOException {
		return new WordTokenizer(text);
	}

}
