package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.Node;
import org.monet.bpi.TransferRequest;
import org.monet.bpi.types.Link;

public class TransferRequestImpl implements TransferRequest {

	@Override
	public void setContent(String content) {
		throw new NotImplementedException();
	}

	@Override
	public void addNode(String name, Node node) {
		throw new NotImplementedException();
	}

	@Override
	public void addNode(String name, Link nodeLink) {
		throw new NotImplementedException();
	}

}
