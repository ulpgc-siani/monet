package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.User;

public class ActionHasPermissions extends Action {

	public ActionHasPermissions() {
	}

	@Override
	public String execute() {
		String username = (String) this.parameters.get(Parameter.USERNAME);
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		FederationLayer federationLayer = componentFederation.getLayer(createConfiguration());

		if (!federationLayer.existsUserByUsername(username))
			return "false";

		User user = federationLayer.loadUserByUsername(username);
		Account account = federationLayer.loadAccount(user.getId());
		if (account == null)
			return "false";

		return String.valueOf(account.hasPermissions());
	}

}
