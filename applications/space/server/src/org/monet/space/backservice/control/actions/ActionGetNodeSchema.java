package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionGetNodeSchema extends Action {

	public ActionGetNodeSchema() {
	}

	@Override
	public String execute() {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		String id = (String) this.parameters.get(Parameter.ID);
		Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);

		return node.getSchema();
	}

}
