package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

public class ActionGetUserNode extends Action {

	public ActionGetUserNode() {
	}

	@Override
	public String execute() {
		String code = (String) this.parameters.get(Parameter.CODE);
		String depth = (String) this.parameters.get(Parameter.DEPTH);
		FederationLayer federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration());
		Node node;

		if (code == null || depth == null)
			return ErrorCode.WRONG_PARAMETERS;

		User user = federationLayer.loadUserByUsername(code);
		node = federationLayer.loadAccount(user.getId()).getEnvironmentNodesByRole().values().iterator().next().get(0);

		return node.serializeToXML(Integer.valueOf(depth));
	}

}
