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

import net.minidev.json.JSONObject;
import org.monet.metamodel.SetDefinition;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeDataRequest;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActionLoadAncestorChildId extends PrintAction {
	private NodeLayer nodeLayer;

	public ActionLoadAncestorChildId() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		final String idAncestor = LibraryRequest.getParameter(Parameter.ANCESTOR, this.request);
		final String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		final Integer index = Integer.valueOf(LibraryRequest.getParameter(Parameter.INDEX, this.request));
		final Node node;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idAncestor == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.EXPORT_NODE);

		node = this.nodeLayer.loadNode(idAncestor);

		if (!this.componentSecurity.canRead(node, this.getAccount()))
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idAncestor);

		NodeDataRequest dataRequest = createNodeDataRequest(null, node.getDefinition().getCode());
		dataRequest.setCodeReference(Dictionary.getInstance().getIndexDefinition(((SetDefinition)node.getDefinition()).getIndex().getValue()).getCode());
		dataRequest.setCodeView(codeView);
		dataRequest.setStartPos(index);
		dataRequest.setLimit(1);

		List<Node> map = new ArrayList<>(this.nodeLayer.requestNodeListItems(idAncestor, dataRequest).values());
		return map.size() > 0 ? map.get(0).getId() : "-1";
	}

}