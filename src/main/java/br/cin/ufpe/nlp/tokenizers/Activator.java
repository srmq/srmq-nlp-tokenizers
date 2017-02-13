package br.cin.ufpe.nlp.tokenizers;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.cin.ufpe.nlp.api.tokenization.TokenizerFactory;

public class Activator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("type", "multiannot");
		context.registerService(TokenizerFactory.class.getName(), new MultipleAnnotationTokenizerFactory(), props);
		
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
