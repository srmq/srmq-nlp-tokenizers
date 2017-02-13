package br.cin.ufpe.nlp.tokenizers;

import java.util.List;
import java.util.Properties;

import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class WordTokenizer extends BaseAnnotationStringTokenizer implements Tokenizer<String> {
	
	private static ThreadLocal<StanfordCoreNLP> pipeline = null;

	public WordTokenizer(String text) {
		super(text, TextAnnotation.class);
	}

	public WordTokenizer(List<CoreLabel> tokens) {
		super(TextAnnotation.class, tokens);
	}

	@Override
	protected synchronized StanfordCoreNLP getPipeline() {
		if (pipeline == null) {
			pipeline = new ThreadLocal<StanfordCoreNLP>() {
				protected StanfordCoreNLP initialValue() {
					Properties props = new Properties();
			        props.put("annotators", "tokenize");				
					return new StanfordCoreNLP(props);
				}
			};
		}
		return pipeline.get();
	}

}
