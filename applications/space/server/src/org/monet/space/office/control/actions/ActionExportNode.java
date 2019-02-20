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

import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionExportNode extends Action {
	private NodeLayer nodeLayer;

	public ActionExportNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		String codeFormat = LibraryRequest.getParameter(Parameter.FORMAT, this.request).toLowerCase();
		String nodes = LibraryRequest.getParameter(Parameter.NODES, this.request);
		String nodeTypes = LibraryRequest.getParameter(Parameter.NODE_TYPES, this.request);
		Configuration configuration = Configuration.getInstance();
		DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
		Date fromDate = null, dtToDate = null;
		String fromDateValue, toDateValue;
		String fileName, content = Strings.EMPTY;
		Node node, filteredNode = new Node();
		NodeList nodeList;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((idNode == null) || (codeFormat == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.EXPORT_NODE);
		}

		try {
			fromDateValue = LibraryRequest.getParameter(Parameter.FROM_DATE, this.request);
			if ((fromDateValue != null) && (!fromDateValue.equals(Strings.EMPTY))) fromDate = date.parse(fromDateValue);

			toDateValue = LibraryRequest.getParameter(Parameter.TO_DATE, this.request);
			if ((toDateValue != null) && (!toDateValue.equals(Strings.EMPTY))) dtToDate = date.parse(toDateValue);
		} catch (ParseException oException) {
			this.agentException.error(oException);
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.EXPORT_NODE, oException);
		}

		node = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(node, this.getAccount())) {
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);
		}

		nodeList = this.nodeLayer.filterNodeList(node, nodes, nodeTypes, fromDate, dtToDate);
		filteredNode = node.clone();
		filteredNode.setId(node.getId());
		filteredNode.setNodeList(nodeList);

		codeFormat = codeFormat.toLowerCase();
		content = this.nodeLayer.exportNode(filteredNode, codeFormat);
		fileName = configuration.getDocumentsDir() + Strings.BAR45 + idNode + Strings.DOT + codeFormat;
		AgentFilesystem.createFile(fileName);
		AgentFilesystem.writeFile(fileName, content);

		return Language.getInstance().getMessage(MessageCode.NODE_EXPORTED);
	}

}