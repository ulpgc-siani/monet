package org.monet.bpi.java;

import org.monet.bpi.FieldLink;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.SearchRequest;

public class FieldLinkImpl extends FieldImpl<Link> implements FieldLink {

	private Node linkedNode = null;

	public Link get() {
		String code = this.getIndicatorValue(Indicator.CODE);
		String value = this.getIndicatorValue(Indicator.VALUE);

		if (code.isEmpty() && value.isEmpty())
			return null;

		return new Link(code, value);
	}

	@Override
	public Term getAsTerm() {
		Link link = this.get();
		if (link == null)
			return null;
		return new Term(link.getId(), link.getLabel());
	}

	@Override
	public Node getNode() {
		if (linkedNode == null && this.get() != null) {
			BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
			NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

			org.monet.space.kernel.model.Node node = nodeLayer.loadNode(this.get().getId());
			NodeImpl linkedNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
			linkedNode.injectNode(node);
			this.linkedNode = linkedNode;
		}
		return this.linkedNode;
	}

	@Override
	public void set(Link value) {
		if (value == null) return;

		String id = value.getId();
		String label = value.getLabel();

		if (id == null && label == null) return;

		if (id == null) {
			SearchRequest searchRequest = new SearchRequest();
			searchRequest.setCondition(value.getLabel());

			NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
			NodeList nodeList = nodeLayer.search(this.nodeId, searchRequest);

			if (nodeList.getCount() <= 0) return;
			id = nodeList.get().values().iterator().next().getId();
		}

		if (label == null) {
			NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
			org.monet.space.kernel.model.Node node = nodeLayer.loadNode(id);
			label = node.getLabel();
		}

		this.setIndicatorValue(Indicator.CODE, id.toString());
		this.setIndicatorValue(Indicator.NODE_LINK, id.toString());
		this.setIndicatorValue(Indicator.VALUE, label.toString());
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

}