package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Importer;
import org.monet.bpi.ImporterService;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.ImporterDefinition;
import org.monet.v3.model.Dictionary;

public class ImporterServiceImpl extends ImporterService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(ImporterServiceImpl service) {
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
	public Importer getImpl(String name) {
		ImporterDefinition definition = (ImporterDefinition) this.dictionary.getDefinition(name);
		ImporterImpl bpiImporter = (ImporterImpl) this.bpiClassLocator.instantiateBehaviour(definition);
		bpiImporter.injectApi(this.api);
		bpiImporter.injectBPIClassLocator(this.bpiClassLocator);
		bpiImporter.injectDictionary(this.dictionary);
		return bpiImporter;
	}

	public static void init() {
	}

}
