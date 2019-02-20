package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.Source;

public class ActionLoadSource extends Action {

	public ActionLoadSource() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		Source source;

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		source = sourceLayer.loadSource(id);

		if (source == null)
			return "";

		return source.serializeToXML();
	}

}
