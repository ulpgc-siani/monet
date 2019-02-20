package client.core.system;

import client.core.model.definition.views.ContainerViewDefinition;

public class ContainerView extends NodeView<ContainerViewDefinition> implements client.core.model.ContainerView {
	private client.core.model.NodeView hostView;

	public ContainerView() {
	}

	public ContainerView(Key key, String label, boolean isDefault, client.core.model.Node scope, client.core.model.NodeView hostView) {
		super(key, label, isDefault, scope);
		this.hostView = hostView;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.ContainerView.CLASS_NAME;
	}

	@Override
	public <T extends client.core.model.NodeView> T getHostView() {
		return (T) hostView;
	}

	@Override
	public <T extends client.core.model.NodeView> void setHostView(T view) {
		this.hostView = view;
	}

}
