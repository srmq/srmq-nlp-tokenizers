package br.cin.ufpe.nlp.tokenizers;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.ufpe.nlp.api.tokenization.TokenPreProcess;
import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public abstract class BaseAnnotationTokenizer<T> implements Tokenizer<T> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected Optional<TokenPreProcess> tokenProcessor;
	
	protected List<CoreLabel> tokens;
	
	protected Iterator<CoreLabel> tokenIterator;
	
	protected List<CoreMap> sentences;
	

	public BaseAnnotationTokenizer(String text) {
		this(text, false);
	}
	
	public BaseAnnotationTokenizer(String text, boolean genSentences) {
		Annotation notes = new Annotation(text);
		try {
			getPipeline().annotate(notes);
		} catch (Throwable t) {
			logger.error("Error while trying to annotate text", t);
		}
		final List<CoreLabel>  theTokens = notes.get(TokensAnnotation.class);
		initTokenizer(theTokens);
		if (genSentences) {
	        this.sentences = notes.get(SentencesAnnotation.class);
		}
	}
	
	private void initTokenizer(List<CoreLabel> tokens) {
		this.tokens = tokens;
		this.tokenIterator = this.tokens.iterator();
		this.tokenProcessor = Optional.empty();
	}
	
	
	public BaseAnnotationTokenizer(List<CoreLabel> tokens) {
		initTokenizer(tokens);
	}

	@Override
	public boolean hasMoreTokens() {
		return this.tokenIterator.hasNext();	}


	@Override
	public void setTokenPreProcessor(TokenPreProcess tokenPreProcessor) {
		this.tokenProcessor = Optional.of(tokenPreProcessor);
	}
	
	protected abstract StanfordCoreNLP getPipeline();

}
