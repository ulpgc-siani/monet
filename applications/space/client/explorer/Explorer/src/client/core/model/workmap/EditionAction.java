package client.core.model.workmap;

import client.core.model.definition.entity.PlaceDefinition.EditionActionDefinition;
import client.core.model.Form;

public interface EditionAction extends Action<EditionActionDefinition> {

	ClassName CLASS_NAME = new ClassName("EditionAction");

	Form getForm();
	void setForm(Form form);

}