package client.core.system;

import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.Node;

public abstract class NodeView<T extends NodeViewDefinition> extends View<T> implements client.core.model.NodeView<T> {
	private Node scope;

	public NodeView() {
	}

	public NodeView(client.core.model.Key key, String label, boolean isDefault, Node scope) {
		super(key, label, isDefault);
		this.scope = scope;
	}

	@Override
	public <T extends Node> T getScope() {
		return (T)this.scope;
	}

	@Override
	public void setScope(Node scope) {
		this.scope = scope;
	}

}
