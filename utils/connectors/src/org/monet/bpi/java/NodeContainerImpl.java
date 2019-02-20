package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeContainer;
import org.monet.bpi.Node;
import org.monet.bpi.NodeContainer;
import org.monet.bpi.types.Location;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.Definition;
import org.monet.metamodel.internal.Ref;

import java.util.ArrayList;
import java.util.List;

public abstract class NodeContainerImpl extends NodeImpl implements NodeContainer, BehaviorNodeContainer {

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
		containedNode.injectApi(this.api);
		containedNode.injectBPIClassLocator(this.bpiClassLocator);
		containedNode.injectDictionary(this.dictionary);
		return containedNode;
	}

	public List<Node> getChildren() {
		ContainerDefinition definition = (ContainerDefinition) this.dictionary.getDefinition(node.getCode());
		List<Node> result = new ArrayList<>();

		for (Ref contain : definition.getContain().getNode())
			result.add(getChildNode(contain.getValue()));

		return result;
	}

	@Override
	public Location getLocation() {
		GeoreferencedImpl.inject(api);
		return GeoreferencedImpl.getLocation(node);
	}

	@Override
	public void setLocation(Location bpilocation) {
		GeoreferencedImpl.setLocation(this.node, bpilocation);
	}

}
