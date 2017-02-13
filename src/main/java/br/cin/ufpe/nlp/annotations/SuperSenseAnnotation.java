package br.cin.ufpe.nlp.annotations;

import edu.stanford.nlp.ling.CoreAnnotation;

public class SuperSenseAnnotation implements CoreAnnotation<String>{
    @Override
    public Class<String> getType() {
      return String.class;
    }
}
