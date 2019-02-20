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

import org.monet.metamodel.NodeDefinition;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.renders.OfficeRender;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;

public class ActionLoadNodeGroupByOptions extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodeGroupByOptions() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idNode == null || code == null || codeView == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_ITEMS);

		Node node = this.nodeLayer.loadNode(idNode);
		NodeDefinition definition = node.getDefinition();

		String view = "";
		if (definition.isCollection())
			view = "collectiongroupbyoptions";
		else if (definition.isCatalog())
			view = "cataloggroupbyoptions";
		else
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_ITEMS);

		if (!this.componentSecurity.canRead(node, this.getAccount()))
			return ErrorCode.READ_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.READ_NODE_PERMISSIONS);

		OfficeRender render = this.rendersFactory.get(view, "edit.html?view=" + codeView, this.getRenderLink(), getAccount());
		render.setTarget(node);
		render.setParameter("groupby", code);
		render.setParameter("standalone", true);

		return render.getOutput();
	}

}