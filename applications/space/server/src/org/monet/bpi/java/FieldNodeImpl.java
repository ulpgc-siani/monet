package org.monet.bpi.java;

import org.monet.bpi.FieldNode;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Indicator;

public class FieldNodeImpl extends FieldImpl<Link> implements FieldNode {

	private Node containedNode;
	NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

	@Override
	public Link get() {
		String code = this.getIndicatorValue(Indicator.CODE);
		String value = this.getIndicatorValue(Indicator.VALUE);

		return new Link(code, value);
	}

	@Override
	public Term getAsTerm() {
		Link link = this.get();
		if (link == null)
			return null;
		return new Term(link.getId(), link.getLabel());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T getNode() {
		if (containedNode == null) {
			BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
			NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

			org.monet.space.kernel.model.Node node = nodeLayer.loadNode(this.get().getId());
			NodeImpl containedNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
			containedNode.injectNode(node);
			this.containedNode = containedNode;
		}
		return (T) this.containedNode;
	}

	@Override
	public void set(Link value) {
		if (value == null) value = emptyLink();
		if (!value.getId().isEmpty()) this.nodeLayer.setParentNode(this.nodeLayer.loadNode(value.getId()), this.nodeLayer.loadNode(this.nodeId));
		this.setIndicatorValue(Indicator.CODE, value.getId());
		this.setIndicatorValue(Indicator.NODE, value.getId());
		this.setIndicatorValue(Indicator.VALUE, value.getLabel());
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Link)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(new Link("", ""));
	}

	private Link emptyLink() {
		Link result = new Link();
		result.setId("");
		result.setLabel("");
		return result;
	}

}