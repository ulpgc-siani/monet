package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.bpi.BPIThesaurus;
import org.monet.bpi.BPIThesaurusService;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.model.Dictionary;

public class BPIThesaurusServiceImpl extends BPIThesaurusService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(BPIThesaurusServiceImpl service) {
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
	public BPIThesaurus get(String name) {
		return null;
	}

}
