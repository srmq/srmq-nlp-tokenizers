package br.cin.ufpe.nlp.tokenizers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import br.cin.ufpe.nlp.api.tokenization.TokenizerFactory;
import edu.stanford.nlp.io.IOUtils;

public abstract class BaseTokenizerFactory<T extends Tokenizer<?>> implements TokenizerFactory<T> {

	public T createFromFile(String filename) throws IOException {
	    Reader r = IOUtils.readerFromString(filename);
	    T result = create(r);
	    IOUtils.closeIgnoringExceptions(r);
	    return result;
	}

	public T createFromFile(File file) throws IOException {
		return createFromFile(file.getAbsolutePath());
	}
	
	public T create(InputStream stream) throws IOException {
		return create(new InputStreamReader(stream));
	}
	
	public T create(InputStream stream, java.lang.String encoding) throws IOException {
	    return create(new InputStreamReader(stream, encoding));		
	}
	
	public T create(Reader reader)  throws IOException {
	    String text = IOUtils.slurpReader(reader);
	    return createFromText(text);
	}
}
