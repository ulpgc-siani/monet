/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.office.control.actions;

import org.monet.space.office.presentation.user.views.ViewSearch;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.SearchNodeResult;
import org.monet.space.kernel.model.SearchRequest;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.constants.Views;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionSearchNodes extends Action {
	private NodeLayer nodeLayer;

	public ActionSearchNodes() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String sCondition = LibraryRequest.getParameter(Parameter.CONDITION, this.request);
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String sMode = LibraryRequest.getParameter(Parameter.MODE, this.request);
		SearchNodeResult searchResult;
		NodeList nodeList;
		DateFormat dfFrom = new SimpleDateFormat("dd/MM/yyyy"), dfTo = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dtFrom = null, dtTo = null;
		String sFromDate, sToDate;
		SearchRequest oSearchRequest = new SearchRequest();
		Node oNode;
		ViewSearch oViewSearch;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		try {
			sFromDate = LibraryRequest.getParameter(Parameter.FROM_DATE, this.request);
			sToDate = LibraryRequest.getParameter(Parameter.TO_DATE, this.request);
			if ((sFromDate != null) && (!sFromDate.equals(Strings.EMPTY))) dtFrom = dfFrom.parse(sFromDate);
			if ((sToDate != null) && (!sToDate.equals(Strings.EMPTY))) dtTo = dfTo.parse(sToDate + " 23:59:59");
		} catch (ParseException oException) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SHARE_NODE, oException);
		}

		if ((id == null) || (sCondition == null) || (sMode == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SEARCH_NODES);
		}

		oSearchRequest.setCondition(sCondition);
		if (dtFrom != null) oSearchRequest.setFromDate(dtFrom);
		if (dtTo != null) oSearchRequest.setToDate(dtTo);

		oNode = this.nodeLayer.loadNode(id);

		nodeList = this.nodeLayer.search(oNode, oSearchRequest);
		searchResult = new SearchNodeResult();
		searchResult.setCondition(sCondition);
		searchResult.setList(nodeList);

		oViewSearch = (ViewSearch) this.viewsFactory.get(Views.SEARCH, this.agentRender, this.codeLanguage);
		oViewSearch.setTarget(searchResult);
		oViewSearch.setCodeTemplate(code);
		oViewSearch.setMode(sMode);

		oNode.setContent(oViewSearch.execute());

		return oNode.toJson().toJSONString();
	}

}