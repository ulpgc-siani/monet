package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.ProviderRequest;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;

public class ProviderRequestImpl implements ProviderRequest {

	@Override
	public void setContent(String content) {
		throw new NotImplementedException();
	}

	@Override
	public void attachFile(String name, File file) {
		throw new NotImplementedException();
	}

	@Override
	public void attachDocument(String name, NodeDocument document) {
		throw new NotImplementedException();
	}

	@Override
	public void attachSchema(String name, Schema schema) {
		throw new NotImplementedException();
	}

	@Override
	public void attachSchema(String name, NodeDocument document) {
		throw new NotImplementedException();
	}

	@Override
	public void attachString(String name, String content) {
		throw new NotImplementedException();
	}

}
