package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.TranslationService;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

public class TranslationServiceImpl extends TranslationService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(TranslationServiceImpl service) {
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
	public String translateImpl(String resourceId, String languageCode) {
		throw new NotImplementedException();
	}

	public static void init() {
	}

}
