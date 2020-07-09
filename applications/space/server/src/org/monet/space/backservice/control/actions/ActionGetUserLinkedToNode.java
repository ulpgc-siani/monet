package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

public class ActionGetUserLinkedToNode extends Action {

	public ActionGetUserLinkedToNode() {
	}

	@Override
	public String execute() {
		String idNode = (String) this.parameters.get(Parameter.ID_NODE);
		FederationLayer federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration());
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;
		User user;

		if (idNode == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(idNode);
		user = federationLayer.loadUserLinkedToNode(node);

		return user != null ? user.serializeToXML() : "";
	}

}
