package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Node;
import org.monet.api.space.backservice.impl.model.Reference;
import org.monet.api.space.backservice.impl.model.ReferenceList;
import org.monet.bpi.Expression;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.OrderExpression;
import org.monet.bpi.Param;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.v3.BPIClassLocator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CollectionIterableImpl implements Iterable<IndexEntry> {

	private IndexDefinition referenceDefinition;
	private String whereQuery;
	private String orderQuery;
	private HashMap<String, Object> whereParameters;
	private int pageSize;
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;
	private Expression whereExpression;
	private OrderExpression orderByExpression;
	private Node node;

	public CollectionIterableImpl(Node node, Expression where, OrderExpression orderBy) {
		this(node, where, orderBy, 10);
	}

	public CollectionIterableImpl(Node node, Expression where, OrderExpression orderBy, int pageSize) {
		this.node = node;
		this.whereExpression = where;
		this.orderByExpression = orderBy;
		this.pageSize = pageSize;
	}

	private void init() {
		Definition definition = this.dictionary.getDefinition(node.getCode());
		Expression where = this.whereExpression;
		OrderExpression orderBy = this.orderByExpression;
		Ref index;

		if (definition instanceof CollectionDefinition) {
			where = (where != null) ? Expression.And(new Param("id_parent").Eq(node.getId()), where) : new Param("id_parent").Eq(node.getId());
			index = ((CollectionDefinition) definition).getIndex();
		} else if (definition instanceof CatalogDefinition) {
			index = ((CatalogDefinition) definition).getIndex();
		} else {
			throw new RuntimeException("Node is not a Collection.");
		}

		this.whereQuery = where != null ? where.toString() : "";
		this.whereParameters = where != null ? where.getParameters() : new HashMap<String, Object>();
		this.orderQuery = orderBy != null ? orderBy.toString() : "";
		this.referenceDefinition = index != null ? (IndexDefinition)this.dictionary.getDefinition(index.getValue()) : new DescriptorDefinition();
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
		this.init();
	}

	@Override
	public Iterator<IndexEntry> iterator() {
		CollectionIteratorImpl iterator = new CollectionIteratorImpl(this.pageSize);
		iterator.injectApi(this.api);
		iterator.injectBpiClassLocator(this.bpiClassLocator);
		iterator.injectDictionary(this.dictionary);
		iterator.init();
		return iterator;
	}

	@SuppressWarnings("unchecked")
	public long getTotalCount() {
		long totalCount = this.api.getNodeReferencesCount(referenceDefinition.getCode(), whereQuery, (HashMap<String, Object>) whereParameters.clone());
		return totalCount;
	}

	private class CollectionIteratorImpl implements Iterator<IndexEntry> {
		private int pageSize;
		private int currentOffset;
		private int totalCount;
		private Iterator<Map.Entry<String, Reference>> currentPage;
		private Reference currentReference;
		private BackserviceApi api;
		private BPIClassLocator bpiClassLocator;
		private Dictionary dictionary;

		public void injectApi(BackserviceApi api) {
			this.api = api;
		}

		public void injectBpiClassLocator(BPIClassLocator bpiClassLocator) {
			this.bpiClassLocator = bpiClassLocator;
		}

		public void injectDictionary(Dictionary dictionary) {
			this.dictionary = dictionary;
		}

		@SuppressWarnings("unchecked")
		public CollectionIteratorImpl(int pageSize) {
			this.pageSize = pageSize;
		}

		public void init() {
			this.currentOffset = -pageSize;
			getNextPage();
			this.totalCount = this.api.getNodeReferencesCount(referenceDefinition.getCode(), whereQuery, (HashMap<String, Object>) whereParameters.clone());
		}

		@SuppressWarnings("unchecked")
		private void getNextPage() {
			this.currentOffset += pageSize;
			ReferenceList referenceList = this.api.getNodeReferences(referenceDefinition.getCode(),
				whereQuery,
				orderQuery,
				(HashMap<String, Object>) whereParameters.clone(),
				currentOffset,
				pageSize);
			this.currentPage = referenceList.get().entrySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return this.currentPage.hasNext() || (currentOffset + pageSize) < totalCount;
		}

		@Override
		public IndexEntry next() {
			if (!this.hasNext()) return null;

			IndexEntryImpl bpiReference = this.bpiClassLocator.instantiateBehaviour(referenceDefinition);

			if (!currentPage.hasNext())
				this.getNextPage();

			currentReference = currentPage.next().getValue();

			bpiReference.injectIndexEntry(currentReference);
			bpiReference.injectApi(this.api);
			bpiReference.injectBPIClassLocator(this.bpiClassLocator);
			bpiReference.injectDictionary(this.dictionary);

			return bpiReference;
		}

		@Override
		public void remove() {
			this.api.removeNode(String.valueOf(this.currentReference.getIdNode()));
		}
	}
}
