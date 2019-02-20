package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionMakeNodeUnEditable extends Action {

	public ActionMakeNodeUnEditable() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		Node node;
		NodeLayer nodeLayer;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		node = nodeLayer.loadNode(id);
		nodeLayer.makeUneditable(node);

		return MessageCode.NODE_MADE_UNEDITABLE;
	}

}
