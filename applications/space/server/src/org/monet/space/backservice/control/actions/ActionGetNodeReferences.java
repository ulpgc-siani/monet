package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.ReferenceList;

import java.util.HashMap;

public class ActionGetNodeReferences extends Action {

	public ActionGetNodeReferences() {
	}

	@Override
	public String execute() {
		String codeReference = (String) this.parameters.get(Parameter.CODE);
		String filter = (String) this.parameters.get(Parameter.FILTER);
		String orderBy = (String) this.parameters.get(Parameter.ORDER_BY);
		String parametersValue = (String) this.parameters.get(Parameter.PARAMETERS);
		int start = Integer.valueOf((String) this.parameters.get(Parameter.START));
		int limit = Integer.valueOf((String) this.parameters.get(Parameter.LIMIT));
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		if (!parametersValue.isEmpty()) {
			String[] parametersArray = LibraryEncoding.decode(parametersValue).split(PARAMETER_SEPARATOR);

			for (int i = 0; i < parametersArray.length; i++) {
				String[] parameterArray = parametersArray[i].split("=");
				parameters.put(parameterArray[0], parameterArray.length > 1 ? parameterArray[1] : "");
			}
		}

		ReferenceList referenceList = nodeLayer.getReferences(codeReference, LibraryEncoding.decode(filter), LibraryEncoding.decode(orderBy), parameters, start, limit);

		return referenceList.serializeToXML();
	}

}
