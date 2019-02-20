package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.api.backservice.impl.model.Node;
import org.monet.api.backservice.impl.model.Reference;
import org.monet.api.backservice.impl.model.ReferenceList;
import org.monet.bpi.BPIExpression;
import org.monet.bpi.BPIOrderExpression;
import org.monet.bpi.BPIParam;
import org.monet.bpi.BPIReference;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.metamodel.CatalogDefinition;
import org.monet.v2.metamodel.CollectionDefinition;
import org.monet.v2.metamodel.Definition;
import org.monet.v2.metamodel.ReferenceDefinition;
import org.monet.v2.model.Dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BPICollectionIterableImpl<R extends BPIReference> implements Iterable<R> {

	private ReferenceDefinition referenceDefinition;
	private String whereQuery;
	private String orderQuery;
	private HashMap<String, Object> whereParameters;
	private Dictionary dictionary;
	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private BPIExpression whereExpression;
	private BPIOrderExpression orderExpression;
	private Node node;

	public BPICollectionIterableImpl(Node node, BPIExpression where, BPIOrderExpression orderBy) {
		this.node = node;
		this.whereExpression = where;
		this.orderExpression = orderBy;
	}

	private void init() {
		String referenceName = null;
		Definition definition = this.dictionary.getDefinition(node.getCode());

		if (definition instanceof CollectionDefinition) {
			this.whereExpression = (this.whereExpression != null) ? BPIExpression.And(new BPIParam("id_parent").Eq(node.getId()), this.whereExpression) : new BPIParam("id_parent").Eq(node.getId());
			referenceName = ((CollectionDefinition) definition).getUse().getReference();
		} else if (definition instanceof CatalogDefinition) {
			referenceName = ((CatalogDefinition) definition).getUse().getReference();
		} else {
			throw new RuntimeException("Node is not a Collection.");
		}

		this.whereQuery = this.whereExpression != null ? this.whereExpression.toString() : "";
		this.whereParameters = this.whereExpression != null ? this.whereExpression.getParameters() : new HashMap<String, Object>();
		this.orderQuery = this.orderExpression != null ? this.orderExpression.toString() : "";
		this.referenceDefinition = this.dictionary.getReferenceDefinition(referenceName);
	}

	@Override
	public Iterator<R> iterator() {
		BPICollectionIteratorImpl iterator = new BPICollectionIteratorImpl();
		iterator.injectApi(this.api);
		iterator.injectDictionary(this.dictionary);
		iterator.injectBPIClassLocator(this.bpiClassLocator);
		iterator.init();
		return iterator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
		init();
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	private class BPICollectionIteratorImpl implements Iterator<R> {
		private static final int pageSize = 10;
		private int currentOffset = -pageSize;
		private int totalCount;
		private Iterator<Map.Entry<String, Reference>> currentPage;
		private Reference currentReference;
		private BackserviceApi api;
		private BPIClassLocator bpiClassLocator;
		private Dictionary dictionary;

		@SuppressWarnings("unchecked")
		public BPICollectionIteratorImpl() {
		}

		public void init() {
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

		@SuppressWarnings("unchecked")
		@Override
		public R next() {
			if(!this.hasNext()) return null;

			BPIReferenceImpl bpiReference = (BPIReferenceImpl)this.bpiClassLocator.getDefinitionInstance(referenceDefinition.getName());

			if(!currentPage.hasNext())
				this.getNextPage();

			currentReference = currentPage.next().getValue();

			bpiReference.injectReference(currentReference);
			bpiReference.injectApi(this.api);
			bpiReference.injectDictionary(this.dictionary);
			bpiReference.injectBPIClassLocator(this.bpiClassLocator);

			return (R)bpiReference;
		}

		@Override
		public void remove() {
			this.api.removeNode(this.currentReference.getIdNode());
		}

		public void injectApi(BackserviceApi api) {
			this.api = api;
		}

		public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
			this.bpiClassLocator = bpiClassLocator;
		}

		public void injectDictionary(Dictionary dictionary) {
			this.dictionary = dictionary;
		}
	}
}
