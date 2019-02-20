package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.bpi.BPIClientService;
import org.monet.bpi.BPIMonetLink;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.model.Dictionary;

public class BPIClientServiceImpl extends BPIClientService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(BPIClientServiceImpl service) {
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
	public void redirectUserTo(BPIMonetLink monetLink) {
	}

	@Override
	public void sendMessageToUser(String message) {
	}
}
