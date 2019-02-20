package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.*;

public abstract class BPINodeCollectionImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>,
	Parent extends BPIBaseNode<?>,
	Reference extends BPIReference>
	extends BPINodeImpl<Parent, Schema>
	implements BPINodeCollection<Parent, Schema>, BPIBehaviorNodeCollection<OperationEnum> {

	public Iterable<Reference> getAll() {
		return get(null);
	}

	public Iterable<Reference> getAll(BPIOrderExpression orderBy) {
		return get(null, orderBy);
	}

	public Iterable<Reference> get(BPIExpression where) {
		return get(where, null);
	}

	public Iterable<Reference> get(BPIExpression where, BPIOrderExpression orderBy) {
		BPICollectionIterableImpl<Reference> bpiReference = new BPICollectionIterableImpl<Reference>(this.node, where, orderBy);
		bpiReference.injectDictionary(this.dictionary);
		bpiReference.injectApi(this.api);
		bpiReference.injectBPIClassLocator(this.bpiClassLocator);
		return bpiReference;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	protected <T extends BPINode<?, ?>> T addNode(String name) {
		String code = this.dictionary.getDefinition(name).getCode();
		Node node = this.api.createNode(code, this.node.getId());
		BPINodeImpl bpiNode = (BPINodeImpl) this.bpiClassLocator.getDefinitionInstance(name);
		bpiNode.injectNode(node);
		bpiNode.injectApi(this.api);
		bpiNode.injectDictionary(this.dictionary);
		bpiNode.injectBPIClassLocator(this.bpiClassLocator);
		return (T) bpiNode;
	}

	@Override
	public <T extends BPINode<?, ?>> void remove(T node) {
		BPINodeImpl<?, ?> nodeImpl = (BPINodeImpl<?, ?>) node;
		if (node.getOwnerId().equals(this.node.getId()))
			this.api.removeNode(nodeImpl.node.getId());
		else
			throw new RuntimeException("This collection doesn't contains the node " + nodeImpl.node.getId());
	}

	;

}
