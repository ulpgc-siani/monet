package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.InsourcingResponse;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDocument;
import org.monet.bpi.Schema;
import org.monet.bpi.types.File;

import java.util.Iterator;

public class InsourcingResponseImpl implements InsourcingResponse {

	@Override
	public String getCode() {
		throw new NotImplementedException();
	}

	@Override
	public String getContent() {
		throw new NotImplementedException();
	}

	@Override
	public File getFile(String name) {
		throw new NotImplementedException();
	}

	@Override
	public NodeDocument getDocument(String name, Class<? extends NodeDocument> documentType) {
		throw new NotImplementedException();
	}

	@Override
	public Schema getSchema(String name, Class<? extends Schema> schemaType) {
		throw new NotImplementedException();
	}

	@Override
	public String getString(String name) {
		throw new NotImplementedException();
	}

	@Override
	public Iterator<String> getAttachmentsKeys() {
		throw new NotImplementedException();
	}

	@Override
	public Node getNode(String name) {
		throw new NotImplementedException();
	}

}
