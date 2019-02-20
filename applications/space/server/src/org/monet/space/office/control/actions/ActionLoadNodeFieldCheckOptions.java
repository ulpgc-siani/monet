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
import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.renders.OfficeRender;

public class ActionLoadNodeFieldCheckOptions extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodeFieldCheckOptions() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String sourceId = LibraryRequest.getParameter(Parameter.SOURCE, this.request);
		String fieldId = LibraryRequest.getParameter(Parameter.ID_FIELD, this.request);
		String field = LibraryRequest.getParameter(Parameter.FIELD, this.request);
		String from = LibraryRequest.getParameter(Parameter.FROM, this.request);
		Node node;
		AttributeList attributeList;

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (id == null || sourceId == null || field == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_FIELD_CHECK_OPTIONS);

		node = this.nodeLayer.loadNode(id);

		if (!this.componentSecurity.canRead(node, this.getAccount()))
			return ErrorCode.READ_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.READ_NODE_PERMISSIONS);

		FormDefinition definition = (FormDefinition) node.getDefinition();
		CheckFieldProperty fieldDefinition = (CheckFieldProperty) definition.getField(field);
		attributeList = this.nodeLayer.getNodeFormCheckFieldOptions(node, sourceId, fieldDefinition, from);

		JSONObject result = new JSONObject();
		result.put("data", attributeList.serializeToXML());
		result.put("view", this.getView(node, sourceId, fieldId, from, fieldDefinition));

		return result.toString();
	}

	private String getView(Node node, String sourceId, String fieldId, String from, CheckFieldProperty fieldDefinition) {
		OfficeRender render = this.rendersFactory.get("fieldcheckoptions", "edit.html", this.getRenderLink(), getAccount());
		render.setParameter("node", node);
		render.setParameter("definition", fieldDefinition);
		render.setParameter("sourceId", sourceId);
		render.setParameter("fieldId", fieldId);
		render.setParameter("from", from);
		return render.getOutput();
	}

}