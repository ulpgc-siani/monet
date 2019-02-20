package org.monet.bpi.java;

import org.monet.bpi.IndexEntry;
import org.monet.bpi.Mapping;
import org.monet.bpi.Node;

public abstract class MappingImpl implements Mapping {

	private IndexEntry entry;
	private Node node;

	void injectIndexEntry(IndexEntry entry) {
		this.entry = entry;
	}

	void injectNode(Node node) {
		this.node = node;
	}

	public IndexEntry genericGetReference() {
		return this.entry;
	}

	public Node genericGetNode() {
		return this.node;
	}

	@Override
	public void calculateMapping() {
	}

}
