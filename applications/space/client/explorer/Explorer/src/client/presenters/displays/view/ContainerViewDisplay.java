package client.presenters.displays.view;

import client.core.model.*;

public class ContainerViewDisplay extends NodeViewDisplay<ContainerView> {

	public static final Type TYPE = new Type("ContainerViewDisplay", NodeViewDisplay.TYPE);

	public ContainerViewDisplay(Node node, ContainerView nodeView) {
        super(node, nodeView);
	}

	@Override
	protected void onInjectServices() {
		if (getView().getHostView() instanceof ProxyView)
			return;

		addHostView();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public static class Builder extends ViewDisplay.Builder<Container, ContainerView> {

		protected static void register() {
			registerBuilder(Container.CLASS_NAME.toString() + ContainerView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Container entity, ContainerView view) {
			return new ContainerViewDisplay(entity, view);
		}
	}

	private void addHostView() {
		ContainerView containerView = getView();
		NodeView hostView = containerView.getHostView();

		addChild(new NodeViewDisplay.Builder().build(hostView.getScope(), hostView));
	}

}
