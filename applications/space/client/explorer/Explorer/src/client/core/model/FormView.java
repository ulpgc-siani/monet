package client.core.model;

import client.core.model.definition.views.FormViewDefinition;

public interface FormView extends NodeView<FormViewDefinition> {

	ClassName CLASS_NAME = new ClassName("FormView");

	ClassName getClassName();
	List<Field> getShows();
}
