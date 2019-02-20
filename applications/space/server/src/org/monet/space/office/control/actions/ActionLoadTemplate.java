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

import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.constants.ViewType;
import org.monet.space.office.presentation.user.constants.Views;
import org.monet.space.office.presentation.user.views.ViewTree;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.NodeList;
import org.monet.space.office.control.constants.Actions;

public class ActionLoadTemplate extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadTemplate() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID_NODE, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if ((idNode == null) || (code == null)) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_TEMPLATE);
		}

		if (code == Common.Codes.TEMPLATE_MODEL_SELECT_NODE_TYPES) {
			NodeList oNodeList = this.nodeLayer.loadNodes(idNode);

			ViewTree oViewTree = (ViewTree) this.viewsFactory.get(Views.VIRTUALTREE, this.agentRender, this.codeLanguage);
			oViewTree.setTarget(oNodeList);
			oViewTree.setType(ViewType.BROWSE);
			oViewTree.setMode(code);

			return oViewTree.execute();
		}

		return Strings.EMPTY;
	}

}