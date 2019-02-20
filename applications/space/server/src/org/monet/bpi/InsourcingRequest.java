package org.monet.bpi;

import org.monet.bpi.types.File;
import org.monet.bpi.types.Link;

public interface InsourcingRequest {

	void setContent(String content);

	void attachFile(String name, File file);

	void attachDocument(String name, NodeDocument document);

	void attachSchema(String name, Schema schema);

	void attachSchema(String name, NodeDocument document);

	void attachString(String name, String content);

	void addNode(String name, Node node);

	void addNode(String name, Link nodeLink);

}
