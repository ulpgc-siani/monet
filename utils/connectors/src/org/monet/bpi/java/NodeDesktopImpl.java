package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeDesktop;
import org.monet.bpi.Node;
import org.monet.bpi.NodeDesktop;
import org.monet.metamodel.Definition;

public abstract class NodeDesktopImpl
	extends NodeImpl
	implements NodeDesktop, BehaviorNodeDesktop {

	String getLinkedNodeId(String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return this.getIndicatorValue(code + ".value");
	}

	String getChildNodeId(String code) {
		if (code == null) return null;
		if (code.length() <= 0) return null;
		if (!code.substring(0, 1).equals("[")) code = "[" + code + "]";
		return this.getIndicatorValue(code + ".value");
	}

	protected Node getChildNode(String name) {

		String code = this.dictionary.getDefinition(name).getCode();

		String id = this.getChildNodeId(code);
		if (id == null || id.isEmpty())
			return null;

		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(id);
		Definition definition = this.dictionary.getDefinition(node.getCode());
		NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(definition);
		containedNode.injectNode(node);
		return (Node) containedNode;
	}

	protected Node getLinkedNode(String name) {
		String code = this.dictionary.getDefinition(name).getCode();

		org.monet.api.space.backservice.impl.model.Node node = this.api.openNode(this.getLinkedNodeId(code));
		Definition definition = this.dictionary.getDefinition(node.getCode());
		NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(definition);
		containedNode.injectNode(node);
		return (Node) containedNode;
	}

}
