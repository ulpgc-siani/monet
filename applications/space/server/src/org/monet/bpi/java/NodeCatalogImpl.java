package org.monet.bpi.java;

import org.monet.bpi.*;

public abstract class NodeCatalogImpl extends NodeSetImpl implements NodeCatalog, BehaviorNodeCatalog {

	protected Iterable<?> genericGetAll() {
		return genericGet(null);
	}

	protected Iterable<?> genericGetAll(OrderExpression orderBy) {
		return genericGet(null, orderBy);
	}

	protected Iterable<?> genericGet(Expression where) {
		return new NodeSetIterableImpl(this.node, where, null);
	}

	protected Iterable<?> genericGet(Expression where, OrderExpression orderBy) {
		return new NodeSetIterableImpl(this.node, where, orderBy);
	}

	protected IndexEntry genericGetFirst(Expression where) {
		NodeSetIterableImpl iterable = new NodeSetIterableImpl(this.node, where, null, 1);
		for (IndexEntry ref : iterable)
			return ref;
		return null;
	}

}
