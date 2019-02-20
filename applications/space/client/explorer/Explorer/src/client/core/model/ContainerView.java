package client.core.model;

import client.core.model.definition.views.ContainerViewDefinition;

public interface ContainerView extends NodeView<ContainerViewDefinition> {

	ClassName CLASS_NAME = new ClassName("ContainerView");

	ClassName getClassName();

	<T extends NodeView> NodeView getHostView();
	<T extends NodeView> void setHostView(T view);
}
