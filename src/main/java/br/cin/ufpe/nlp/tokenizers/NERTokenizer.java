package br.cin.ufpe.nlp.tokenizers;

import java.util.List;
import java.util.Properties;

import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class NERTokenizer extends BaseAnnotationStringTokenizer implements Tokenizer<String> {
	
	private static ThreadLocal<StanfordCoreNLP> pipeline = null;

	public NERTokenizer(String text) {
		super(text, NamedEntityTagAnnotation.class);
	}

	public NERTokenizer(List<CoreLabel> tokens) {
		super(NamedEntityTagAnnotation.class, tokens);
	}

	@Override
	protected synchronized StanfordCoreNLP getPipeline() {
		if (pipeline == null) {
			pipeline = new ThreadLocal<StanfordCoreNLP>() {
				protected StanfordCoreNLP initialValue() {
					Properties props = new Properties();
			        props.put("annotators", "tokenize, ssplit, pos, lemma, ner");				
					return new StanfordCoreNLP(props);
				}
			};
		}
		return pipeline.get();
	}

}
