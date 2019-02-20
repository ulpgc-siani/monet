package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.model.Datastore;

public class ActionOpenDatastore extends Action {

	public ActionOpenDatastore() {
	}

	@Override
	public String execute() {
		String name = (String) this.parameters.get(Parameter.NAME);
		Datastore datastore;

		if (name == null)
			return ErrorCode.WRONG_PARAMETERS;

		datastore = ComponentDatawareHouse.getInstance().getDatastoreLayer().load(name);

		return datastore.serializeToXML();
	}

}
