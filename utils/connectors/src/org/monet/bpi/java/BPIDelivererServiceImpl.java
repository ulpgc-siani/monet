package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.bpi.BPIBaseNodeDocument;
import org.monet.bpi.BPIDelivererService;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.model.Dictionary;

import java.net.URI;

public class BPIDelivererServiceImpl extends BPIDelivererService {
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;

	public static void injectInstance(BPIDelivererServiceImpl service) {
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

	private byte[] getDocumentBytes(BPIBaseNodeDocument<?> document) {
		return null;
	}

	@Override
	public void deliver(URI url, BPIBaseNodeDocument<?> document) throws Exception {
	}

	@Override
	public void deliverToMail(URI from, URI to, String subject, String body, BPIBaseNodeDocument<?> document) {
	}

}
