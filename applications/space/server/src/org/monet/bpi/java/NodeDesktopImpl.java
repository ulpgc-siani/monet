package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDesktop;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDesktop;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.model.BusinessUnit;

public abstract class NodeDesktopImpl
	extends NodeImpl
	implements NodeDesktop, BehaviorNodeDesktop {

	String getLinkedNodeId(String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return this.node.getIndicatorValue(code + ".value");
	}

	String getChildNodeId(String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return this.node.getIndicatorValue(code + ".value");
	}

	protected Node getChildNode(String name) {
		BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

		String code = BusinessUnit.getInstance()
			.getBusinessModel()
			.getDictionary()
			.getDefinitionCode(name);

		String id = this.getChildNodeId(code);
		if (id == null || id.isEmpty())
			return null;

		org.monet.space.kernel.model.Node node = this.nodeLayer.loadNode(id);
		NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
		containedNode.injectNode(node);
		return (Node) containedNode;
	}

	protected Node getLinkedNode(String name) {
		BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

		String code = BusinessUnit.getInstance()
			.getBusinessModel()
			.getDictionary()
			.getDefinitionCode(name);

		org.monet.space.kernel.model.Node node = this.nodeLayer.loadNode(this.getLinkedNodeId(code));
		NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
		containedNode.injectNode(node);
		return (Node) containedNode;
	}

}
