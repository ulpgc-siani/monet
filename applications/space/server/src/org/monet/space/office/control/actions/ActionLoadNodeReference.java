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

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

public class ActionLoadNodeReference extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodeReference() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = (String) LibraryRequest.getParameter(Parameter.ID, this.request);
		String sMode = (String) LibraryRequest.getParameter(Parameter.MODE, this.request);
		Node oNode;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			id = (String) this.request.getAttribute(Parameter.ID);
		}
		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_REFERENCE);
		}

		if (sMode == null) {
			id = (String) this.request.getAttribute(Parameter.ID);
		} else sMode = sMode.replace(Strings.AMPERSAND_HTML, Strings.AMPERSAND);

		oNode = this.nodeLayer.loadNode(id);

		if (!this.componentSecurity.canRead(oNode, this.getAccount())) {
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, id);
		}

		return oNode.getReference().toJson().toJSONString();
	}

}