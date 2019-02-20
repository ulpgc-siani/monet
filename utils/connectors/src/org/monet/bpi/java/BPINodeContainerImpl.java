package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBehaviorNodeContainer;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPINodeContainer;
import org.monet.bpi.BPISchema;
import org.monet.v2.metamodel.Definition;

public abstract class BPINodeContainerImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>,
	Parent extends BPIBaseNode<?>>
	extends BPINodeImpl<Parent, Schema>
	implements BPINodeContainer<Parent, Schema>, BPIBehaviorNodeContainer<OperationEnum> {

	String getChildNodeId(String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return this.getIndicatorValue(code + ".value");
	}

	protected BPIBaseNode<?> getChildNode(String name) {
		Definition definition = this.dictionary.getDefinition(name);
		String code = definition.getCode();
		Node node = this.api.openNode(this.getChildNodeId(code));
		BPIBaseNodeImpl<?> containedNode = this.bpiClassLocator.getDefinitionInstance(definition.getName());
		containedNode.injectNode(node);
		containedNode.injectApi(this.api);
		containedNode.injectDictionary(this.dictionary);
		containedNode.injectBPIClassLocator(this.bpiClassLocator);
		return (BPIBaseNode<?>) containedNode;
	}

}
