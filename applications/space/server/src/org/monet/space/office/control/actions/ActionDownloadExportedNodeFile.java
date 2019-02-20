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

import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.utils.MimeTypes;


public class ActionDownloadExportedNodeFile extends Action {
	private NodeLayer nodeLayer;

	public ActionDownloadExportedNodeFile() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		String codeFormat = LibraryRequest.getParameter(Parameter.FORMAT, this.request).toLowerCase();
		String documentName, content;
		MimeTypes mimeTypes = MimeTypes.getInstance();
		Node node;
		String fileName;
		Configuration configuration = Configuration.getInstance();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((idNode == null) || (codeFormat == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.DOWNLOAD_EXPORTED_NODE_FILE);
		}

		fileName = configuration.getDocumentsDir() + Strings.BAR45 + idNode + Strings.DOT + codeFormat;
		if (!AgentFilesystem.existFile(fileName)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.DOWNLOAD_EXPORTED_NODE_FILE);
		}

		node = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(node, this.getAccount())) {
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);
		}

		documentName = node.getReference().getLabel().replace(Strings.SPACE, Strings.UNDERLINED);

		this.response.setContentType(mimeTypes.get(codeFormat));

		try {
			content = AgentFilesystem.readFile(fileName);
			this.response.setHeader("Content-Disposition", "attachment; filename=" + documentName + Strings.DOT + codeFormat);
			this.response.getWriter().print(content);
		} catch (Exception oException) {
			this.agentException.error(oException);
			throw new DataException(ErrorCode.DOWNLOAD_DOCUMENT, idNode + Strings.SPACE + codeFormat, oException);
		}

		return null; // Avoid controller getting response writer
	}

}