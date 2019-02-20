package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;

public class ActionOpenNodes extends Action {

	public ActionOpenNodes() {
	}

	@Override
	public String execute() {
		String nodeIdsValue = (String) this.parameters.get(Parameter.IDS);
		String depth = (String) this.parameters.get(Parameter.DEPTH);
		NodeList nodeList = new NodeList();
		Node node;

		if (nodeIdsValue == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		if (depth == null) depth = "-1";

		String[] nodeIds = nodeIdsValue.split(",");
		for (String nodeId : nodeIds) {
			node = ComponentPersistence.getInstance().getNodeLayer().loadNode(nodeId);
			nodeList.add(node);
		}

		return nodeList.serializeToXML(Integer.valueOf(depth));
	}

}
