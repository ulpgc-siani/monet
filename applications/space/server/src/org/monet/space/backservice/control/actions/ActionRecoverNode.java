package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionRecoverNode extends Action {

	public ActionRecoverNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		NodeLayer nodeLayer;
		Node node;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		nodeLayer.recoverNodeFromTrash(id);
		node = nodeLayer.loadNode(id);

		return node.serializeToXML();
	}

}
