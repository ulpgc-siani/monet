package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionCreateNode extends Action {

	public ActionCreateNode() {
	}

	@Override
	public String execute() {
		String idParent = (String) this.parameters.get(Parameter.PARENT);
		String type = (String) this.parameters.get(Parameter.TYPE);
		Node node, parent;
		NodeLayer nodeLayer;

		if (idParent == null || type == null) return ErrorCode.WRONG_PARAMETERS;

		nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		parent = null;
		if ((idParent != null) && (!idParent.equals("-1"))) parent = nodeLayer.loadNode(idParent);
		node = nodeLayer.addNode(type, parent);

		return node.serializeToXML();
	}

}
