package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.FeederUri;
import org.monet.space.kernel.model.Source;

public class ActionLocateSource extends Action {

	public ActionLocateSource() {
	}

	@Override
	public String execute() {
		String code = (String) this.parameters.get(Parameter.CODE);
		String url = (String) this.parameters.get(Parameter.URL);
		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		Source source;

		if (code == null)
			return ErrorCode.WRONG_PARAMETERS;

		if (url != null && url.isEmpty())
			url = null;

		code = this.dictionary.getDefinitionCode(code);
		source = sourceLayer.locateSource(code, FeederUri.build(url));

		if (source == null)
			return "";

		return source.serializeToXML();
	}

}
