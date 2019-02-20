package org.monet.space.frontservice.control.actions;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.agents.AgentRestfullClient.RequestParameter;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.TermList;

public class ActionSource extends Action {
	private SourceLayer sourceLayer;

	public ActionSource() {
		super();
		this.sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
	}

	public String execute() {
		TermList termList;
		String subOperation = (String) this.parameters.get(RequestParameter.ACTION);
		DataRequest dataRequest = this.getDataRequest();

		String codeSource = dataRequest.getCode();
		String parent = dataRequest.getParameter(DataRequest.FROM);
		if (parent != null && parent.isEmpty())
			parent = null;

		Source<SourceDefinition> source = this.sourceLayer.locateSource(codeSource, null);

		if (subOperation.equals("search"))
			termList = source.searchTerms(dataRequest);
		else if (subOperation.equals("load"))
			termList = source.loadTerms(dataRequest, true);
		else if (subOperation.equals("synchronize"))
			termList = source.loadTerms(true);
		else
			termList = null;

		if (termList == null)
			return "";

		return termList.serializeToXML(true).toString();
	}

	protected DataRequest getDataRequest() {
		DataRequest dataRequest = new DataRequest();
		dataRequest.setCode((String) this.parameters.get(RequestParameter.SOURCE_NAME));
		if (this.parameters.containsKey(RequestParameter.MODE))
			dataRequest.addParameter(DataRequest.MODE, (String) this.parameters.get(RequestParameter.MODE));
		if (this.parameters.containsKey(RequestParameter.FLATTEN))
			dataRequest.addParameter(DataRequest.FLATTEN, (String) this.parameters.get(RequestParameter.FLATTEN));
		if (this.parameters.containsKey(RequestParameter.DEPTH))
			dataRequest.addParameter(DataRequest.DEPTH, (String) this.parameters.get(RequestParameter.DEPTH));
		if (this.parameters.containsKey(RequestParameter.FROM))
			dataRequest.addParameter(DataRequest.FROM, (String) this.parameters.get(RequestParameter.FROM));
		if (this.parameters.containsKey(RequestParameter.FILTERS))
			dataRequest.addParameter(DataRequest.FILTERS, (String) this.parameters.get(RequestParameter.FILTERS));
		if (this.parameters.containsKey(RequestParameter.SEARCH_TEXT))
			dataRequest.setCondition((String) this.parameters.get(RequestParameter.SEARCH_TEXT));
		if (this.parameters.containsKey(RequestParameter.START_POS))
			dataRequest.setStartPos(Integer.parseInt((String) this.parameters.get(RequestParameter.START_POS)));
		if (this.parameters.containsKey(RequestParameter.COUNT))
			dataRequest.setLimit(Integer.parseInt((String) this.parameters.get(RequestParameter.COUNT)));
		return dataRequest;
	}

}
