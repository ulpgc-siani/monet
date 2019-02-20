package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;

import java.util.HashMap;

public class ActionGetNodeReferencesCount extends Action {

	public ActionGetNodeReferencesCount() {
	}

	@Override
	public String execute() {
		String codeReference = (String) this.parameters.get(Parameter.CODE);
		String filter = (String) this.parameters.get(Parameter.FILTER);
		String parametersValue = (String) this.parameters.get(Parameter.PARAMETERS);
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		if (!parametersValue.isEmpty()) {
			String[] parametersArray = LibraryEncoding.decode(parametersValue).split(PARAMETER_SEPARATOR);

			for (int i = 0; i < parametersArray.length; i++) {
				String[] parameterArray = parametersArray[i].split("=");
				parameters.put(parameterArray[0], parameterArray.length > 1 ? parameterArray[1] : "");
			}
		}

		return String.valueOf(nodeLayer.getReferencesCount(codeReference, LibraryEncoding.decode(filter), parameters));
	}

}
