package org.monet.bpi;

import org.monet.bpi.types.Link;

public interface IndexEntry {

	public Node getIndexedNode();

	public void save();

	public Link toLink();

}