package client.services.http.builders.workmap;

import client.services.http.builders.NodeBuilder;
import client.services.http.builders.RoleBuilder;
import client.core.system.workmap.DelegationAction;

import client.services.http.HttpInstance;

import static client.core.model.workmap.DelegationAction.*;

public class DelegationActionBuilder extends ActionBuilder<client.core.model.workmap.DelegationAction> {

	@Override
	public client.core.model.workmap.DelegationAction build(HttpInstance instance) {
		if (instance == null)
			return null;

		DelegationAction action = new DelegationAction();
		initialize(action, instance);
		return action;
	}

	@Override
	public void initialize(client.core.model.workmap.DelegationAction object, HttpInstance instance) {
		super.initialize(object, instance);

		DelegationAction action = (DelegationAction)object;

		if (instance.getDate("failureDate") != null)
			action.setFailureDate(instance.getDate("failureDate"));

		action.setStep(Step.fromString(instance.getString("step")));
		action.setOrderNode(new NodeBuilder<>().build(instance.getObject("orderNode")));

		if (instance.getObject("role") != null)
			action.setRole(new RoleBuilder<>().build(instance.getObject("role")));
	}

}
