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
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionAddNode extends Action {
	private NodeLayer nodeLayer;

	public ActionAddNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String idParent = LibraryRequest.getParameter(Parameter.ID_PARENT, this.request);
		String sMode = LibraryRequest.getParameter(Parameter.MODE, this.request);
		ActionLoadNode oAction;
		Node node, parent;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (code == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.ADD_NODE);

		parent = this.nodeLayer.loadNode(idParent);

		if (!this.componentSecurity.canCreate(parent, this.getAccount()))
			throw new NodeAccessException(ErrorCode.WRITE_NODE_PERMISSIONS, parent.getId());

		node = this.nodeLayer.addNode(code, parent);

		if (sMode != null) {
			this.request.setAttribute(Parameter.ID, node.getId());
			this.request.setAttribute(Parameter.MODE, sMode);
			oAction = (ActionLoadNode) this.actionsFactory.get(Actions.LOAD_NODE, this.request, this.response);
			return oAction.execute();
		}

		return Language.getInstance().getMessage(MessageCode.NODE_ADDED);
	}

}