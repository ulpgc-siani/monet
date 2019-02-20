package client.core.model;

import client.core.model.definition.views.SetViewDefinition;

public interface SetView extends NodeView<SetViewDefinition> {

	ClassName CLASS_NAME = new ClassName("SetView");

	ClassName getClassName();
	Index getIndex();

}
