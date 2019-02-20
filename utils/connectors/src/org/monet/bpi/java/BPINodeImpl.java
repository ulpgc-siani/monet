package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPINode;
import org.monet.bpi.BPISchema;
import org.monet.v2.metamodel.Definition;

public class BPINodeImpl<Owner extends BPIBaseNode<?>,
	Schema extends BPISchema>
	extends BPIBaseNodeImpl<Schema>
	implements BPINode<Owner, Schema> {

	@Override
	public String getOwnerId() {
		return this.node.getParentId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Owner getOwner() {

		String ownerId = this.node.getParentId();
		if (ownerId == null || ownerId.equals("-1"))
			return null;

		Node node = this.api.openNode(ownerId);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		BPIBaseNodeImpl<?> containedNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		containedNode.injectNode(node);
		containedNode.injectApi(this.api);
		containedNode.injectDictionary(this.dictionary);
		containedNode.injectBPIClassLocator(this.bpiClassLocator);
		return (Owner) containedNode;
	}

	@Override
	public void setOwner(Owner owner) {
		BPIBaseNodeImpl<?> ownerImpl = (BPIBaseNodeImpl<?>) owner;
		this.api.saveNodeParent(node.getId(), ownerImpl.node.getId());
	}

	@Override
	public boolean isPrototype() {
		if (this.node.isPrototype()) return true;
		return this.node.getParentId() == null;
	}

}
