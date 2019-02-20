package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.SearchRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionSearchNodes extends Action {

	public ActionSearchNodes() {
	}

	@Override
	public String execute() {
		String id = (String) this.parameters.get(Parameter.ID);
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
		String fromValue = (String) this.parameters.get(Parameter.FROM);
		String toValue = (String) this.parameters.get(Parameter.TO);
		NodeLayer nodeLayer;
		SearchRequest searchRequest;
		NodeList nodeList;
		Date from = null, to = null;
		Node node;

		if (id == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		try {
			if ((fromValue != null) && (!fromValue.equals(Strings.EMPTY))) from = dateFormat.parse(fromValue);
			if ((toValue != null) && (!toValue.equals(Strings.EMPTY))) to = dateFormat.parse(toValue);
		} catch (ParseException oException) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		searchRequest = new SearchRequest();
		searchRequest.setFromDate(from);
		searchRequest.setToDate(to);

		node = nodeLayer.loadNode(id);
		nodeList = nodeLayer.search(node, searchRequest);

		return nodeList.serializeToXML();
	}

}
