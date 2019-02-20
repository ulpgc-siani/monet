package org.monet.bpi.java;

import org.monet.bpi.*;
import org.monet.metamodel.Definition;
import org.monet.space.kernel.model.Dictionary;

import java.util.HashSet;

public abstract class NodeCollectionImpl extends NodeSetImpl implements NodeCollection, BehaviorNodeCollection {

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

	public int remove(Expression where) {
		HashSet<String> ids = new HashSet<String>();
		for (IndexEntry ref : new NodeSetIterableImpl(this.node, where, null)) {
			ids.add(ref.toLink().getId());
		}

		for (String id : ids)
			this.nodeLayer.deleteNode(id);

		return ids.size();
	}

	public Node addNode(Class<? extends Node> definitionClass) {
		Definition definition = Dictionary.getInstance().getDefinition(definitionClass.getName());
		org.monet.space.kernel.model.Node node = nodeLayer.addNode(definition.getCode(), this.node);
		NodeImpl bpiNode = this.bpiClassLocator.instantiateBehaviour(definition);
		bpiNode.injectNode(node);
		return bpiNode;
	}

	@Override
	public void remove(Node node) {
		NodeImpl nodeImpl = (NodeImpl) node;
		if (node.getOwnerId().equals(this.node.getId()))
			this.nodeLayer.deleteNode(nodeImpl.node);
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
		return genericGetAll();
	}

}
