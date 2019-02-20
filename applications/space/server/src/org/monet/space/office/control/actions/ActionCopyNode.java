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
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionCopyNode extends Action {
	private NodeLayer nodeLayer;

	public ActionCopyNode() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String idParent = LibraryRequest.getParameter(Parameter.ID_PARENT, this.request);
		String sMode = LibraryRequest.getParameter(Parameter.MODE, this.request);
		ActionLoadNode oAction;
		Node newNode, sourceNode, parentNode;
		Language oLanguage = Language.getInstance();
		String date, label, description;
		SimpleDateFormat sdfFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((id == null) || (idParent == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.COPY_NODE);
		}

		parentNode = this.nodeLayer.loadNode(idParent);

		sourceNode = this.nodeLayer.loadNode(id);
		if (!this.componentSecurity.canRead(sourceNode, this.getAccount())) {
			throw new NodeAccessException(ErrorCode.READ_NODE_PERMISSIONS, sourceNode.getId());
		}

		if (!this.componentSecurity.canCreate(parentNode, this.getAccount())) {
			throw new NodeAccessException(ErrorCode.WRITE_NODE_PERMISSIONS, parentNode.getId());
		}
		newNode = this.nodeLayer.addNode(sourceNode.getCode(), parentNode);

		date = sdfFormat.format(new Date());

		if (sourceNode.isPrototype()) {
			label = sourceNode.getLabel();
			description = sourceNode.getDescription();
		} else {
			label = oLanguage.getLabel(LabelCode.CLONE_FROM, codeLanguage) + Strings.SPACE + sourceNode.getLabel();
			description = sourceNode.getDescription() + Strings.SPACE + oLanguage.getLabel(LabelCode.CLONE_AT, this.codeLanguage) + Strings.SPACE + date + Strings.DOT;
		}

		this.nodeLayer.copyNode(newNode, sourceNode, label, description);

		if (sMode != null) {
			this.request.setAttribute(Parameter.ID, newNode.getId());
			this.request.setAttribute(Parameter.MODE, sMode);
			oAction = (ActionLoadNode) this.actionsFactory.get(Actions.LOAD_NODE, this.request, this.response);
			return oAction.execute();
		}

		return newNode.getId();
	}

}