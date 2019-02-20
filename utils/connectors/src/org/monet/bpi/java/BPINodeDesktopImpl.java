package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBehaviorNodeDesktop;
import org.monet.bpi.BPINodeDesktop;
import org.monet.bpi.BPISchema;
import org.monet.v2.metamodel.Definition;

public abstract class BPINodeDesktopImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>,
	Parent extends BPIBaseNode<?>>
	extends BPINodeImpl<Parent, Schema>
	implements BPINodeDesktop<Parent, Schema>, BPIBehaviorNodeDesktop<OperationEnum> {

	String getLinkedNodeId(String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return this.getIndicatorValue(code + ".value");
	}

	protected BPIBaseNode<?> getLinkedNode(String name) {
		Definition definition = this.dictionary.getDefinition(name);
		String code = definition.getCode();

		Node node = this.api.openNode(this.getLinkedNodeId(code));
		BPIBaseNodeImpl<?> containedNode = bpiClassLocator.getDefinitionInstance(definition.getName());
		containedNode.injectNode(node);
		containedNode.injectApi(this.api);
		containedNode.injectDictionary(this.dictionary);
		containedNode.injectBPIClassLocator(this.bpiClassLocator);
		return (BPIBaseNode<?>) containedNode;
	}

}
