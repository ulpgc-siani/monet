package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.DelivererService;
import org.monet.bpi.NodeDocument;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URI;


public class DelivererServiceImpl extends DelivererService {
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public static void injectInstance(DelivererServiceImpl service) {
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

	private byte[] getDocumentBytes(NodeDocument document) {
		throw new NotImplementedException();
	}

	@Override
	public void deliver(URI url, NodeDocument document) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public void deliverToMail(URI from, URI to, String subject, String body, NodeDocument document) {
		throw new NotImplementedException();
	}

}