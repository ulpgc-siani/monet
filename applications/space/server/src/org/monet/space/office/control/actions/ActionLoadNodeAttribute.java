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

import org.monet.metamodel.FormDefinition;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryPath;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;

public class ActionLoadNodeAttribute extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodeAttribute() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID_NODE, this.request);
		String path = LibraryRequest.getParameter(Parameter.PATH, this.request);
		String[] pathArray = path.split(LibraryPath.SPLIT_SEPARATOR);
		Node node;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (idNode == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SAVE_NODE_ATTRIBUTE);
		}

		node = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(node, this.getAccount())) {
			return ErrorCode.WRITE_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.WRITE_NODE_PERMISSIONS);
		}

		Attribute attribute = node.getAttribute(pathArray[0]);
		if (attribute == null) {
			FormDefinition definition = (FormDefinition) node.getDefinition();
			AttributeList attributeList = definition.buildAttributes();
			attribute = attributeList.get(pathArray[0]);
		}

		return attribute.serializeToXML();
	}

}