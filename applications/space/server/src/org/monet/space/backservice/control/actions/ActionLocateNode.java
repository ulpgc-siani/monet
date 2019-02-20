package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionLocateNode extends Action {

	private static final int MAX_DEPTH = 1000;

	public ActionLocateNode() {
	}

	@Override
	public String execute() {
		String id;
		String code = (String) this.parameters.get(Parameter.CODE);
		int depth = this.parameters.containsKey(Parameter.DEPTH)?Integer.valueOf((String)this.parameters.get(Parameter.DEPTH)):MAX_DEPTH;
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node;

		if (code == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		code = this.dictionary.getDefinitionCode(code);

		id = nodeLayer.locateNodeId(code);
		if ((id == null) || (id.isEmpty())) return "";

		node = nodeLayer.loadNode(id);

		return node.serializeToXML(depth);
	}

}
