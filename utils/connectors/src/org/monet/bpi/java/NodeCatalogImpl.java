package org.monet.bpi.java;

import org.monet.bpi.*;

public abstract class NodeCatalogImpl extends NodeImpl implements NodeCatalog, BehaviorNodeCatalog {

	protected Iterable<?> genericGetAll() {
		return genericGet(null);
	}

	protected Iterable<?> genericGetAll(OrderExpression orderBy) {
		return genericGet(null, orderBy);
	}

	protected Iterable<?> genericGet(Expression where) {
		CollectionIterableImpl indexEntries = new CollectionIterableImpl(this.node, where, null);
		indexEntries.injectApi(this.api);
		indexEntries.injectBPIClassLocator(this.bpiClassLocator);
		indexEntries.injectDictionary(this.dictionary);
		return indexEntries;
	}

	protected Iterable<?> genericGet(Expression where, OrderExpression orderBy) {
		CollectionIterableImpl indexEntries = new CollectionIterableImpl(this.node, where, orderBy);
		indexEntries.injectApi(this.api);
		indexEntries.injectBPIClassLocator(this.bpiClassLocator);
		indexEntries.injectDictionary(this.dictionary);
		return indexEntries;
	}

	protected IndexEntry genericGetFirst(Expression where) {
		CollectionIterableImpl iterable = new CollectionIterableImpl(this.node, where, null, 1);

		iterable.injectApi(this.api);
		iterable.injectBPIClassLocator(this.bpiClassLocator);
		iterable.injectDictionary(this.dictionary);

		for (IndexEntry ref : iterable)
			return ref;
		return null;
	}

	@Override
	public long getCount(Expression where) {
		CollectionIterableImpl indexEntries = new CollectionIterableImpl(this.node, where, null);
		indexEntries.injectApi(this.api);
		indexEntries.injectBPIClassLocator(this.bpiClassLocator);
		indexEntries.injectDictionary(this.dictionary);
		return indexEntries.getTotalCount();
	}

}
