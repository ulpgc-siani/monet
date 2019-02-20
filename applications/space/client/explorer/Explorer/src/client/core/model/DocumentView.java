package client.core.model;

import client.core.model.definition.views.DocumentViewDefinition;

public interface DocumentView extends NodeView<DocumentViewDefinition> {

	ClassName CLASS_NAME = new ClassName("DocumentView");

	ClassName getClassName();

}
