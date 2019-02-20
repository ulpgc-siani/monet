package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.metamodel.Definition;

import java.util.HashSet;

public abstract class NodeCollectionImpl extends NodeImpl implements NodeCollection, BehaviorNodeCollection {

	protected Iterable<?> genericGetAll() {
		return genericGet(null);
	}

	protected Iterable<?> genericGetAll(OrderExpression orderBy) {
		return genericGet(null, orderBy);
	}

	protected Iterable<?> genericGet(Expression where) {
		CollectionIterableImpl iterable = new CollectionIterableImpl(this.node, where, null);
		iterable.injectApi(this.api);
		iterable.injectBPIClassLocator(this.bpiClassLocator);
		iterable.injectDictionary(this.dictionary);
		return iterable;
	}

	protected Iterable<?> genericGet(Expression where, OrderExpression orderBy) {
		CollectionIterableImpl iterable = new CollectionIterableImpl(this.node, where, orderBy);
		iterable.injectApi(this.api);
		iterable.injectBPIClassLocator(this.bpiClassLocator);
		iterable.injectDictionary(this.dictionary);
		return iterable;
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

	public int remove(Expression where) {
		HashSet<String> ids = new HashSet<String>();
		CollectionIterableImpl iterable = new CollectionIterableImpl(this.node, where, null);
		iterable.injectApi(this.api);
		iterable.injectBPIClassLocator(this.bpiClassLocator);
		iterable.injectDictionary(this.dictionary);

		for (IndexEntry ref : iterable) {
			ids.add(ref.toLink().getId());
		}

		for (String id : ids)
			this.api.removeNode(id);

		return ids.size();
	}

	@Override
	public long getCount(Expression where) {
		CollectionIterableImpl iterable = new CollectionIterableImpl(this.node, where, null);
		iterable.injectApi(this.api);
		iterable.injectBPIClassLocator(this.bpiClassLocator);
		iterable.injectDictionary(this.dictionary);
		return iterable.getTotalCount();
	}

	public Node addNode(Class<? extends Node> definitionClass) {
		Definition definition = this.dictionary.getDefinition(definitionClass.getName());
		org.monet.api.space.backservice.impl.model.Node node = this.api.createNode(definition.getCode(), this.node.getId());
		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectApi(this.api);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectNode(node);
		return bpiNode;
	}

	@Override
	public void remove(Node node) {
		NodeImpl nodeImpl = (NodeImpl) node;
		if (node.getOwnerId().equals(this.node.getId()))
			this.api.removeNode(nodeImpl.node.getId());
		else
			throw new RuntimeException("This collection doesn't contains the node " + nodeImpl.node.getId());
	}

	@Override
	public void onItemAdded(Node newItem) {
	}

	@Override
	public void onItemRemoved(Node removedItem) {
	}


	@Override
	public Iterable<?> getAll() {
		return genericGet(null);
	}
}
