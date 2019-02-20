package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionResetNodeForm extends Action {

	public ActionResetNodeForm() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);
		nodeLayer.resetNodeForm(node);

		return MessageCode.NODE_FORM_RESET;
	}

}
