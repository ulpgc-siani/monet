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
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionSaveNode extends Action {
	private NodeLayer nodeLayer;

	public ActionSaveNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String data = LibraryRequest.getParameter(Parameter.DATA, this.request);
		String mode = LibraryRequest.getParameter(Parameter.MODE, this.request);
		ActionLoadNode action;
		Node node;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((id == null) || (data == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SAVE_NODE);
		}

        data = LibraryString.cleanNonAsciiCharacters(data);
		node = this.nodeLayer.loadNode(id);

		if (!this.componentSecurity.canWrite(node, this.getAccount())) {
			return ErrorCode.WRITE_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.WRITE_NODE_PERMISSIONS);
		}

		this.nodeLayer.saveNode(node, data);

		if (mode != null) {
			this.request.setAttribute(Parameter.MODE, mode);
			action = (ActionLoadNode) this.actionsFactory.get(Actions.LOAD_NODE, this.request, this.response);
			return action.execute();
		}

		return Language.getInstance().getMessage(MessageCode.NODE_SAVED);
	}

}