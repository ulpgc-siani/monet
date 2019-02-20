package client.core.system.workmap;

import client.core.model.definition.entity.PlaceDefinition.EditionActionDefinition;
import client.core.model.Form;

public class EditionAction extends Action<EditionActionDefinition> implements client.core.model.workmap.EditionAction {
	private Form form;

	public EditionAction() {
	}

	public EditionAction(Form form, EditionActionDefinition definition) {
		super(definition);
		this.form = form;
	}

	@Override
	public Form getForm() {
		return this.form;
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.workmap.EditionAction.CLASS_NAME;
	}

}