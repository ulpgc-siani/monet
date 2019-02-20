package client.presenters.displays.view;

import client.core.model.Node;
import client.core.model.NodeView;

public abstract class NodeViewDisplay<ViewType extends NodeView> extends ViewDisplay<Node, ViewType> {

	public static final Type TYPE = new Type("NodeViewDisplay", ViewDisplay.TYPE);

	public NodeViewDisplay(Node node, ViewType nodeView) {
        super(node, nodeView);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	protected boolean isEditable() {
		return getView().getScope().isEditable();
	}

}
