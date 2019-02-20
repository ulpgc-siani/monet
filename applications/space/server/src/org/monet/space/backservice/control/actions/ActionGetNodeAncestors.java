package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.NodeList;

public class ActionGetNodeAncestors extends Action {

	public ActionGetNodeAncestors() {
	}

	@Override
	public String execute() {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		String id = (String) this.parameters.get(Parameter.ID);
		org.monet.space.kernel.model.Node node;
		NodeList nodeList;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);
		nodeList = nodeLayer.loadNodeAncestors(node);

		return nodeList.serializeToXML();
	}

}
