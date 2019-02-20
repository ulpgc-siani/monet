package org.monet.bpi;

public interface NodeCollection extends NodeSet {

	public int remove(Expression where);

	public void remove(Node node);

	public Node addNode(Class<? extends Node> definition);

	public Iterable<?> getAll();

}
