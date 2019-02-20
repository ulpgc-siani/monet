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

import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.monet.metamodel.*;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

public class ActionLoadNodePrintAttributes extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodePrintAttributes() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idNode == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.PRINT_NODE);

		Node node = this.nodeLayer.loadNode(idNode);

		if (!this.componentSecurity.canRead(node, this.getAccount()))
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);

		Dictionary dictionary = BusinessModel.getInstance().getDictionary();
		NodeDefinition definition = node.getDefinition();

		if (!(definition instanceof SetDefinition))
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.PRINT_NODE);

		SetDefinition.SetViewProperty setViewDefinition = (SetDefinition.SetViewProperty) definition.getNodeView(codeView);
		if (setViewDefinition.getShow().getIndex() == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.PRINT_NODE);

		IndexDefinition indexDefinition = dictionary.getIndexDefinition(((SetDefinition) definition).getIndex().getValue());
		SetDefinitionBase.SetViewPropertyBase.ShowProperty showDefinition = setViewDefinition.getShow();
		String nameReferenceView = showDefinition.getIndex().getWithView().getValue();
		IndexDefinitionBase.IndexViewProperty referenceViewDefinition = indexDefinition.getView(nameReferenceView);

		JSONArray result = new JSONArray();
		for (AttributeProperty attributeDefinition : indexDefinition.getAttributes(referenceViewDefinition)) {
			JSONObject item = new JSONObject();
			item.put("code", attributeDefinition.getCode());
			item.put("label", attributeDefinition.getLabel());
			item.put("type", attributeDefinition.getType().toString());
			result.add(item);
		}

		return result.toString();
	}

}