package org.monet.bpi;

import java.net.URI;

public abstract class DelivererService {

	protected static DelivererService instance;

	public static DelivererService getInstance() {
		return instance;
	}

	public abstract void deliver(URI url, NodeDocument document) throws Exception;

	public abstract void deliverToMail(URI from, URI to, String subject, String body, NodeDocument document);

}