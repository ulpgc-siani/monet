package org.monet.bpi.java;

import org.monet.bpi.InsourcingRequest;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;
import org.monet.bpi.types.Link;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class InsourcingRequestImpl implements InsourcingRequest {

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

	@Override
	public void addNode(String name, Node node) {
		throw new NotImplementedException();
	}

	@Override
	public void addNode(String name, Link nodeLink) {
		throw new NotImplementedException();
	}

}
