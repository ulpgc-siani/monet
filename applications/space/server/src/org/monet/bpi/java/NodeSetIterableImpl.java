package org.monet.bpi.java;

import org.monet.bpi.Expression;
import org.monet.bpi.IndexEntry;
import org.monet.bpi.OrderExpression;
import org.monet.bpi.Param;
import org.monet.metamodel.CatalogDefinition;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Reference;
import org.monet.space.kernel.model.ReferenceList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NodeSetIterableImpl implements Iterable<IndexEntry> {

	private final IndexDefinition referenceDefinition;
	private final String whereQuery;
	private final String orderQuery;
	private final HashMap<String, Object> whereParameters;
	private final NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	private final BPIClassLocator classLocator = BPIClassLocator.getInstance();
	private int pageSize;

	public NodeSetIterableImpl(Node node, Expression where, OrderExpression orderBy) {
		this(node, where, orderBy, 10);
	}

	public NodeSetIterableImpl(Node node, Expression where, OrderExpression orderBy, int pageSize) {
		String referenceName = null;

		if (node.isCollection()) {
			where = (where != null) ? Expression.And(new Param("id_parent").Eq(node.getId()), where) : new Param("id_parent").Eq(node.getId());
			referenceName = ((CollectionDefinition) node.getDefinition()).getIndex().getValue();
		} else if (node.isCatalog()) {
			referenceName = ((CatalogDefinition) node.getDefinition()).getIndex().getValue();
		} else {
			throw new RuntimeException("Node is not a Collection.");
		}

		this.whereQuery = where != null ? where.toString() : "";
		this.whereParameters = where != null ? where.getParameters() : new HashMap<String, Object>();
		this.orderQuery = orderBy != null ? orderBy.toString() : "";
		this.referenceDefinition = BusinessUnit.getInstance()
			.getBusinessModel()
			.getDictionary()
			.getIndexDefinition(referenceName);

		this.pageSize = pageSize;
	}

	@Override
	public Iterator<IndexEntry> iterator() {
		return new CollectionIteratorImpl(this.pageSize);
	}

	@SuppressWarnings("unchecked")
	public long getTotalCount() {
		long totalCount = nodeLayer.getReferencesCount(referenceDefinition.getCode(), whereQuery, (HashMap<String, Object>) whereParameters.clone());
		return totalCount;
	}

	private class CollectionIteratorImpl implements Iterator<IndexEntry> {
		private int pageSize;
		private int currentOffset;
		private int totalCount;
		private Iterator<Map.Entry<String, Reference>> currentPage;
		private Reference currentReference;

		@SuppressWarnings("unchecked")
		public CollectionIteratorImpl(int pageSize) {
			this.pageSize = pageSize;
			this.currentOffset = -pageSize;
			getNextPage();
			this.totalCount = nodeLayer.getReferencesCount(referenceDefinition.getCode(), whereQuery, (HashMap<String, Object>) whereParameters.clone());
		}

		@SuppressWarnings("unchecked")
		private void getNextPage() {
			this.currentOffset += pageSize;
			ReferenceList referenceList = nodeLayer.getReferences(referenceDefinition.getCode(),
				whereQuery,
				orderQuery,
				(HashMap<String, Object>) whereParameters.clone(),
				currentOffset,
				pageSize);
			this.currentPage = referenceList.entryIterator();
		}

		@Override
		public boolean hasNext() {
			return this.currentPage.hasNext() || (currentOffset + pageSize) < totalCount;
		}

		@Override
		public IndexEntry next() {
			if (!this.hasNext()) return null;

			IndexEntry bpiReference = (IndexEntry) classLocator.instantiateBehaviour(referenceDefinition);

			if (!currentPage.hasNext())
				this.getNextPage();

			currentReference = currentPage.next().getValue();
			((IndexEntryImpl) bpiReference).injectIndexEntry(currentReference);
			return bpiReference;
		}

		@Override
		public void remove() {
			nodeLayer.deleteNode(this.currentReference.getIdNode().textValue());
		}
	}
}
