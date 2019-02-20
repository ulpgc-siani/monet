package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.ExportItem;
import org.monet.space.kernel.model.ExportList;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

public class ActionExportNodes extends Action {

	public ActionExportNodes() {
	}

	@Override
	public String execute() {
		String nodeIdsValue = (String) this.parameters.get(Parameter.IDS);
		ExportList list = new ExportList();
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		if (nodeIdsValue == null)
			return ErrorCode.WRONG_PARAMETERS;

		String[] nodeIds = nodeIdsValue.split(",");
		for (String nodeId : nodeIds) {
			try {
				ExportItem exportItem = new ExportItem();
				Node node = nodeLayer.loadNode(nodeId);
				User owner = node.getOwner();

				exportItem.setId(nodeId);
				exportItem.setType(node.getCode());
				exportItem.setOwner(owner != null ? owner.getName() : "");
				exportItem.setContent(nodeLayer.exportNode(nodeId));

				list.add(exportItem);
			} catch (Exception exception) {
				AgentLogger.getInstance().error(exception);
			}
		}

		return list.serializeToXML();
	}

}
