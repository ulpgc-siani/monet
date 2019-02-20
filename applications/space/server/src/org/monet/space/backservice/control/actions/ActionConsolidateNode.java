package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionConsolidateNode extends Action {

	public ActionConsolidateNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		ComponentDocuments componentDocuments = ComponentDocuments.getInstance();
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = nodeLayer.loadNode(id);
		componentDocuments.consolidateDocument(node, false);
		nodeLayer.makeUneditable(node);

		return node.serializeToXML();
	}

}
