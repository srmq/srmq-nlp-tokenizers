package br.cin.ufpe.nlp.tokenizers;

import java.util.List;
import java.util.Properties;

import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class LemmaTokenizer extends BaseAnnotationStringTokenizer implements Tokenizer<String> {
	
	private static ThreadLocal<StanfordCoreNLP> pipeline = null;

	public LemmaTokenizer(String text) {
		super(text, LemmaAnnotation.class);
	}

	public LemmaTokenizer(List<CoreLabel> tokens) {
		super(LemmaAnnotation.class, tokens);
	}

	@Override
	protected synchronized StanfordCoreNLP getPipeline() {
		if (pipeline == null) {
			pipeline = new ThreadLocal<StanfordCoreNLP>() {
				protected StanfordCoreNLP initialValue() {
					Properties props = new Properties();
			        props.put("annotators", "tokenize, ssplit, pos, lemma");				
					return new StanfordCoreNLP(props);
				}
			};
		}
		return pipeline.get();
	}

}
