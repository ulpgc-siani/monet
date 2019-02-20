package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Datastore;
import org.monet.bpi.DatastoreService;
import org.monet.v3.BPIClassLocator;
import org.monet.metamodel.Definition;
import org.monet.v3.model.Dictionary;

public class DatastoreServiceImpl extends DatastoreService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(DatastoreServiceImpl service) {
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

	public static void init() {
	}

	@Override
	public DatastoreImpl getImpl(String name) {
		Datastore datastore = this.api.openDatastore(name);

		Definition definition = this.dictionary.getDefinition(datastore.getCode());
		DatastoreImpl bpiDatastore = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiDatastore.injectApi(this.api);
		bpiDatastore.injectBpiClassLocator(this.bpiClassLocator);
		bpiDatastore.injectDictionary(this.dictionary);
		bpiDatastore.injectDatastore(datastore);

		return bpiDatastore;
	}

}
