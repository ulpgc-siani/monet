package client.services.http.builders.workmap;

import client.core.system.workmap.WaitAction;

import client.services.http.HttpInstance;

public class WaitActionBuilder extends ActionBuilder<client.core.model.workmap.WaitAction> {

	@Override
	public client.core.model.workmap.WaitAction build(HttpInstance instance) {
		if (instance == null)
			return null;

		WaitAction action = new WaitAction();
		initialize(action, instance);
		return action;
	}

	@Override
	public void initialize(client.core.model.workmap.WaitAction object, HttpInstance instance) {
		super.initialize(object, instance);

		WaitAction action = (WaitAction)object;
		action.setDueDate(instance.getDate("dueDate"));
	}
}
