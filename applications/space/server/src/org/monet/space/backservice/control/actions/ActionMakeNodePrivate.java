package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;

public class ActionMakeNodePrivate extends Action {

	public ActionMakeNodePrivate() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		org.monet.space.kernel.model.Node monetNode;
		NodeLayer nodeLayer;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		monetNode = nodeLayer.loadNode(id);
		nodeLayer.makeNodePrivate(monetNode);

		return MessageCode.NODE_MADE_PRIVATE;
	}

}
