package br.cin.ufpe.nlp.tokenizers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.client.RestTemplate;

import br.cin.ufpe.nlp.annotations.SuperSenseAnnotation;
import br.cin.ufpe.nlp.api.tokenization.Tokenizer;
import br.cin.ufpe.nlp.util.AnnotatedDocument;
import br.cin.ufpe.nlp.util.AnnotatedSentence;
import br.cin.ufpe.nlp.util.AnnotatedToken;
import br.cin.ufpe.nlp.util.TokenAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class MultipleAnnotationTokenizer extends BaseAnnotationTokenizer<AnnotatedToken>
		implements Tokenizer<AnnotatedToken> {

	private static ThreadLocal<StanfordCoreNLP> pipeline = null;
	
	private String superSenseServerURL = "http://localhost:8081/supersenses/";
	
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
	
	public MultipleAnnotationTokenizer(List<CoreLabel> tokens) {
		super(tokens);
	}
	
	public MultipleAnnotationTokenizer(String text) throws IOException {
		super(text, true);
		addSuperSenses();
	}
	
	private void addSuperSenses() throws IOException {
		AnnotatedDocument doc = new AnnotatedDocument();
		for (CoreMap sentence : sentences) {
			AnnotatedSentence annotSentence = new AnnotatedSentence();
			List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
	        for (CoreLabel token: tokens) {
	        	// this is the text of the token
	            String word = token.get(TextAnnotation.class);
	        	AnnotatedToken annotToken = new AnnotatedToken(word);
	            String pos = token.get(PartOfSpeechAnnotation.class);
	            annotToken.addAnnotation(TokenAnnotation.POSTAG, pos);
	            int charOffsetBegin = token.get(CharacterOffsetBeginAnnotation.class);
	            annotToken.addAnnotation(TokenAnnotation.CHAROFFSETBEGIN, Integer.toString(charOffsetBegin));
	            annotSentence.addToken(annotToken);
	        }
	        doc.addSentence(annotSentence);
		}
		RestTemplate restTemplate = new RestTemplate();
		AnnotatedDocument result = restTemplate.postForObject(this.superSenseServerURL, doc, AnnotatedDocument.class);
		Map<Integer, String> offSetsToSenses = new HashMap<Integer, String>(this.tokens.size());
		for (AnnotatedSentence sentence : result.getSentences()) {
			for (AnnotatedToken token : sentence.getTokens()) {
				final String offset = token.getAnnotations().get(TokenAnnotation.CHAROFFSETBEGIN);
				if (offset == null) throw new IllegalStateException("Token came back from SuperSenseServer without offset annotation");
				final int intOffset = Integer.parseInt(offset);
				final String superSense = token.getAnnotations().get(TokenAnnotation.SUPERSENSE);
				if (superSense != null) {
					offSetsToSenses.put(intOffset, superSense);
				}
			}
		}
		for (CoreLabel token : this.tokens) {
			int charOffsetBegin = token.get(CharacterOffsetBeginAnnotation.class);
			String superSense = offSetsToSenses.get(charOffsetBegin);
			if (superSense != null) {
				token.set(SuperSenseAnnotation.class, superSense);
			}
		}
	}

	@Override
	public AnnotatedToken nextToken() {
		final CoreLabel nextToken = tokenIterator.next();
		String tokenText = nextToken.get(TextAnnotation.class);
		if (tokenProcessor.isPresent()) {
			tokenText = tokenProcessor.get().preProcess(tokenText);
		}
		
		final String tokenPOS = nextToken.get(PartOfSpeechAnnotation.class);
		final String tokenLemma = nextToken.get(LemmaAnnotation.class);
		final String tokenNER = nextToken.get(NamedEntityTagAnnotation.class);
		final int charOffsetBegin = nextToken.get(CharacterOffsetBeginAnnotation.class);
		final String superSense = nextToken.get(SuperSenseAnnotation.class);
		
		AnnotatedToken result = new AnnotatedToken(tokenText);
		if (tokenPOS != null)
			result.addAnnotation(TokenAnnotation.POSTAG, tokenPOS);
		if (tokenLemma != null)
			result.addAnnotation(TokenAnnotation.LEMMA, tokenLemma);
		if (tokenNER != null)
			result.addAnnotation(TokenAnnotation.NER, tokenNER);
		if (superSense != null)
			result.addAnnotation(TokenAnnotation.SUPERSENSE, superSense);
		
		result.addAnnotation(TokenAnnotation.CHAROFFSETBEGIN, Integer.toString(charOffsetBegin));

		return result;
	}

	public String getSuperSenseServerURL() {
		return superSenseServerURL;
	}

	public void setSuperSenseServerURL(String superSenseServerURL) {
		this.superSenseServerURL = superSenseServerURL;
	}
	
	
	
}
