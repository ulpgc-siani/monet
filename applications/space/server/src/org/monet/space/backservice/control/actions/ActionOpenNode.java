package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.Node;

public class ActionOpenNode extends Action {

	public ActionOpenNode() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String depth = (String) this.parameters.get(Parameter.DEPTH);
		Node node;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		if (depth == null) depth = "-1";

		node = ComponentPersistence.getInstance().getNodeLayer().loadNode(id);

		return node.serializeToXML(Integer.valueOf(depth));
	}

}
