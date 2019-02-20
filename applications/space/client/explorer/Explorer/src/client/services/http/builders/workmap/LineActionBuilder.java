package client.services.http.builders.workmap;

import client.core.system.workmap.LineAction;

import client.services.http.HttpInstance;

public class LineActionBuilder extends ActionBuilder<client.core.model.workmap.LineAction> {

	@Override
	public client.core.model.workmap.LineAction build(HttpInstance instance) {
		if (instance == null)
			return null;

		LineAction action = new LineAction();
		initialize(action, instance);
		return action;
	}

	@Override
	public void initialize(client.core.model.workmap.LineAction object, HttpInstance instance) {
		super.initialize(object, instance);

		LineAction action = (LineAction)object;
		action.setDueDate(instance.getDate("dueDate"));
	}
}
