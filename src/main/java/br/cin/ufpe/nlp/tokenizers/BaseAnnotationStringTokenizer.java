package br.cin.ufpe.nlp.tokenizers;

import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreLabel;

public abstract class BaseAnnotationStringTokenizer extends BaseAnnotationTokenizer<String> {
	
	protected Class<? extends CoreAnnotation<String>> annotationClass;

	public BaseAnnotationStringTokenizer(String text, Class<? extends CoreAnnotation<String>> annotationClass) {
		super(text);
		this.annotationClass = annotationClass;
	}
	
	public BaseAnnotationStringTokenizer(Class<? extends CoreAnnotation<String>> annotationClass, List<CoreLabel> tokens) {
		super(tokens);
		this.annotationClass = annotationClass;
	}

	@Override
	public String nextToken() {
		final CoreLabel nextToken = tokenIterator.next();
		String tokenText = nextToken.get(this.annotationClass);
		if (tokenProcessor.isPresent()) {
			tokenText = tokenProcessor.get().preProcess(tokenText);
		}
		return tokenText;

	}
}
