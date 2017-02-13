package br.cin.ufpe.nlp.tokenizers;

import java.util.List;
import java.util.Properties;

import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class POSTokenizer extends BaseAnnotationStringTokenizer implements Tokenizer<String> {
	
	private static ThreadLocal<StanfordCoreNLP> pipeline = null;

	public POSTokenizer(String text) {
		super(text, PartOfSpeechAnnotation.class);
	}

	public POSTokenizer(List<CoreLabel> tokens) {
		super(PartOfSpeechAnnotation.class, tokens);
	}

	@Override
	protected synchronized StanfordCoreNLP getPipeline() {
		if (pipeline == null) {
			pipeline = new ThreadLocal<StanfordCoreNLP>() {
				protected StanfordCoreNLP initialValue() {
					Properties props = new Properties();
			        props.put("annotators", "tokenize, ssplit, pos");				
					return new StanfordCoreNLP(props);
				}
			};
		}
		return pipeline.get();
	}

}
