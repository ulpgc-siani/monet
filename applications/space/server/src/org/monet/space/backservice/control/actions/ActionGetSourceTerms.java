package org.monet.space.backservice.control.actions;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.TermList;

public class ActionGetSourceTerms extends Action {

	public ActionGetSourceTerms() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		String parent = (String) this.parameters.get(Parameter.PARENT);

		if (id == null)
			return ErrorCode.WRONG_PARAMETERS;

		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		Source<SourceDefinition> source = sourceLayer.loadSource(id);
		TermList termList = sourceLayer.loadSourceTerms(source, createRequest(parent), true);

		return termList.serializeToXML();
	}

	private DataRequest createRequest(String parent) {
		DataRequest dataRequest = new DataRequest();
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.MODE, DataRequest.Mode.TREE);

		if (parent != null)
			dataRequest.addParameter(DataRequest.FROM, parent);

		return dataRequest;
	}

}
