package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Source;
import org.monet.bpi.SourceService;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

public class SourceServiceImpl extends SourceService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(SourceServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	@Override
	protected Source getImpl(String name, String url) {
		SourceDefinition definition = (SourceDefinition)this.dictionary.getDefinition(name);
		org.monet.api.space.backservice.impl.model.Source source = this.api.locateSource(definition.getCode(), url);

		if (source == null) return null;

		SourceImpl bpiSource = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiSource.injectSource(source);
		bpiSource.injectApi(this.api);
		bpiSource.injectDictionary(this.dictionary);
		bpiSource.injectBPIClassLocator(this.bpiClassLocator);

		return bpiSource;

	}

	public static void init() {
	}

}
