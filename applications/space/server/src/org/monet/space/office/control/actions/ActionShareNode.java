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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionShareNode extends Action {
	private NodeLayer nodeLayer;

	public ActionShareNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String idUsers = LibraryRequest.getParameter(Parameter.USERS, this.request);
		String description = LibraryRequest.getParameter(Parameter.DESCRIPTION, this.request);
		DateFormat expireDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date expireDate = null;
		String expireDateValue;
		Node node;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		try {
			expireDateValue = LibraryRequest.getParameter(Parameter.EXPIRE_DATE, this.request);
			if ((expireDateValue != null) && (!expireDateValue.equals(Strings.EMPTY)))
				expireDate = expireDateFormat.parse(expireDateValue);
		} catch (ParseException oException) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SHARE_NODE, oException);
		}

		if ((id == null) || (idUsers == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.SHARE_NODE);
		}

		node = this.nodeLayer.loadNode(id);

		if (!this.componentSecurity.canRead(node, this.getAccount())) {
			throw new NodeAccessException(ErrorCode.READ_NODE_PERMISSIONS, node.getId());
		}

		this.nodeLayer.shareNode(node, idUsers, description, expireDate);

		return Language.getInstance().getMessage(MessageCode.NODE_SHARED);
	}

}