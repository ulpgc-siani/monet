package org.monet.bpi.java;


import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Exporter;
import org.monet.bpi.ExporterService;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.ExporterDefinition;

public class ExporterServiceImpl extends ExporterService {
	private BackserviceApi api;
	private org.monet.v3.model.Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(ExporterServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(org.monet.v3.model.Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	@Override
	public Exporter getImpl(String name) {
		ExporterDefinition definition = (ExporterDefinition)this.dictionary.getDefinition(name);
		ExporterImpl bpiExporter = (ExporterImpl) this.bpiClassLocator.instantiateBehaviour(definition);
		bpiExporter.injectApi(this.api);
		bpiExporter.injectBPIClassLocator(this.bpiClassLocator);
		bpiExporter.injectDictionary(this.dictionary);
		return bpiExporter;
	}

	public static void init() {
	}

}
