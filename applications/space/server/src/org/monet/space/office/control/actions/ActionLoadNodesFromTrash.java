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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.kernel.model.ReferenceAttribute;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadNodesFromTrash extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodesFromTrash() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		NodeList nodeList;
		String start = LibraryRequest.getParameter(Parameter.START, this.request);
		String limit = LibraryRequest.getParameter(Parameter.LIMIT, this.request);
		DataRequest dataRequest = new DataRequest();
		String attributes, rows = new String();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		try {
			dataRequest.setStartPos(Integer.valueOf(start));
		} catch (NumberFormatException oException) {
			dataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			dataRequest.setStartPos(0);
		}

		try {
			dataRequest.setLimit(Integer.valueOf(limit));
		} catch (NumberFormatException oException) {
			dataRequest.setLimit(10);
		} catch (ClassCastException oException) {
			dataRequest.setLimit(10);
		}

		nodeList = this.nodeLayer.loadNodesFromTrash(dataRequest);

		for (Node node : nodeList.get().values()) {
			attributes = "";
			attributes += "id:\"" + node.getId() + "\",";
			for (ReferenceAttribute<?> attribute : node.getReference().getAttributes().values()) {
				attributes += attribute.getCode() + ":\"" + attribute.getValueAsString() + "\",";
			}
			if (attributes.length() > 0) attributes = attributes.substring(0, attributes.length() - 1);
			rows += "{" + attributes + "},";
		}
		if (rows.length() > 0) rows = rows.substring(0, rows.length() - 1);

		return "{nrows: " + nodeList.getTotalCount() + ", rows:[" + rows + "]}";
	}

}