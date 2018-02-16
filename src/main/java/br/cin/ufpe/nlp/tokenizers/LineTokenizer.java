package br.cin.ufpe.nlp.tokenizers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.ufpe.nlp.api.tokenization.TokenPreProcess;
import br.cin.ufpe.nlp.api.tokenization.Tokenizer;

public class LineTokenizer implements Tokenizer<String> {
	private static Logger logger = LoggerFactory.getLogger(LineTokenizer.class);
	
	private BufferedReader bufR;
	
	private boolean isClosed;
	
	private String nextToken = null;

	public LineTokenizer(String text) {
		this.bufR = new BufferedReader(new StringReader(text));
		this.isClosed = false;
		this.tokenPreprocessor = null;
	}
	
	private TokenPreProcess tokenPreprocessor;

	@Override
	public boolean hasMoreTokens() {
		boolean ret = false;
		if (!isClosed) {
			try {
				this.nextToken = bufR.readLine();
				if (nextToken == null) {
					this.bufR.close();
					isClosed = true;
				} else {
					ret = true;
					if (this.tokenPreprocessor != null) {
						this.nextToken = this.tokenPreprocessor.preProcess(nextToken);
					}
				}
			} catch (IOException e) {
				final String msg = "IOException when parsing string argument"; 
				logger.error(msg, e);
				throw new IllegalStateException(msg, e);
			}
		}
		return ret;
	}

	@Override
	public String nextToken() {
		if (isClosed) {
			throw new NoSuchElementException("No more tokens");
		}
		String result = nextToken;
		if (result == null) {
			if (!hasMoreTokens()) {
				throw new NoSuchElementException("No more tokens");
			} else {
				result = nextToken;
				nextToken = null;
			}
		} else {
			this.nextToken = null;
		}
		return result;
	}

	@Override
	public void setTokenPreProcessor(TokenPreProcess tokenPreProcessor) {
		this.tokenPreprocessor = tokenPreProcessor;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (this.bufR != null) {
			this.bufR.close();
		}
	}

}
