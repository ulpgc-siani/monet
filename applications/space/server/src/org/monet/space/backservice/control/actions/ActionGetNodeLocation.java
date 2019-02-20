package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.model.map.Location;

public class ActionGetNodeLocation extends Action {

	public ActionGetNodeLocation() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		org.monet.space.kernel.model.Node node;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		node = ComponentPersistence.getInstance().getNodeLayer().loadNode(id);
		Location location = node.getLocation();

		return location != null ? location.serializeToXML() : "";
	}

}
