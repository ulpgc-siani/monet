package client.services.http.builders.workmap;

import client.services.http.builders.FormBuilder;
import client.core.system.workmap.EditionAction;

import client.services.http.HttpInstance;

public class EditionActionBuilder extends ActionBuilder<client.core.model.workmap.EditionAction> {

	@Override
	public client.core.model.workmap.EditionAction build(HttpInstance instance) {
		if (instance == null)
			return null;

		EditionAction action = new EditionAction();
		initialize(action, instance);
		return action;
	}

	@Override
	public void initialize(client.core.model.workmap.EditionAction object, HttpInstance instance) {
		super.initialize(object, instance);

		EditionAction action = (EditionAction)object;
		action.setForm(new FormBuilder().build(instance.getObject("form")));
	}

}
