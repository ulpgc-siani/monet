package org.monet.bpi;

public interface NodeCollection extends Node {

	public long getCount(Expression where);

	public int remove(Expression where);

	public void remove(Node node);

	public Node addNode(Class<? extends Node> definition);

	public Iterable<?> getAll();

}
