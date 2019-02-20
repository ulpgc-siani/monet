package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentDocuments;

public class ActionGetNodeDocumentContentType extends Action {

	public ActionGetNodeDocumentContentType() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		return ComponentDocuments.getInstance().getDocumentContentType(id);
	}

}
