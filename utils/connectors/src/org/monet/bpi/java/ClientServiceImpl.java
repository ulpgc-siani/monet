package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.ClientService;
import org.monet.bpi.MonetLink;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;
import org.apache.commons.lang.NotImplementedException;

public class ClientServiceImpl extends ClientService {

	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(ClientServiceImpl service) {
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
	protected void redirectUserToImpl(MonetLink monetLink) {
		throw new NotImplementedException();
	}

	@Override
	protected void sendMessageToUserImpl(String message) {
		throw new NotImplementedException();
	}
}
