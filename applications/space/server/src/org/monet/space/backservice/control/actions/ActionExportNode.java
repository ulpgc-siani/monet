package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.ExportItem;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.User;

public class ActionExportNode extends Action {

	public ActionExportNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		ExportItem exportItem = new ExportItem();
		Node node = nodeLayer.loadNode(id);
		User owner = node.getOwner();

		exportItem.setId(id);
		exportItem.setType(node.getCode());
		exportItem.setOwner(owner != null ? owner.getName() : "");
		exportItem.setContent(nodeLayer.exportNode(id));

		return exportItem.serializeToXML();
	}

}
