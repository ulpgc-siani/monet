package org.monet.bpi;

import java.net.URI;
import java.util.Map;

public abstract class DelivererService {

	protected static DelivererService instance;

	public static DelivererService getInstance() {
		return instance;
	}

	public abstract void deliver(URI url, NodeDocument document) throws Exception;

	public abstract void deliver(URI url, Map<String, String> params) throws Exception;

	public abstract void deliverJson(URI url, Map<String, Object> params) throws Exception;

	public abstract void deliverToMail(URI from, URI to, String subject, String body, NodeDocument document);

}