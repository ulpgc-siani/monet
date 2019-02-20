package org.monet.bpi.java;

import org.monet.bpi.IndexEntry;
import org.monet.bpi.Node;
import org.monet.bpi.types.Link;
import org.monet.metamodel.AttributeProperty;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Reference;
import org.monet.space.kernel.model.ReferenceAttribute;

public class IndexEntryImpl implements IndexEntry {

	Reference index;
	org.monet.space.kernel.model.Node node;
	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
	NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

	void injectIndexEntry(Reference index) {
		this.index = index;
	}

	void injectNode(org.monet.space.kernel.model.Node node) {
		this.node = node;
	}

	private void loadNode() {
		if (this.node == null)
			this.node = this.nodeLayer.loadNode(String.valueOf(this.index.getIdNode().intValue()));
	}

	@SuppressWarnings("unchecked")
	protected <T> T getAttribute(String name) {
		AttributeProperty attributeDefinition = this.index.getDefinition().getAttribute(name);

		if (attributeDefinition == null)
			return null;

		String code = attributeDefinition.getCode();
		ReferenceAttribute<?> attribute = this.index.getAttribute(code);
		if (attribute == null)
			return null;

		return (T)attribute.getValue();
	}

	protected <T> void setAttribute(String name, T value) {
		this.index.setAttributeValue(name, value);
	}

	public Node getIndexedNode() {
		loadNode();

		NodeImpl indexedNode = bpiClassLocator.instantiateBehaviour(node.getDefinition());
		indexedNode.injectNode(node);
		return indexedNode;
	}

	@Override
	public void save() {
		if (this.node == null)
			this.node = this.nodeLayer.loadNode(this.index.getIdNode().textValue());
		this.nodeLayer.saveNodeReference(this.node, this.index);
	}

	@Override
	public Link toLink() {
		loadNode();
		return new Link(this.node.getId(), this.node.getLabel());
	}

}
