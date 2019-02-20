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

import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.office.presentation.user.constants.ViewType;
import org.monet.space.office.presentation.user.constants.Views;
import org.monet.space.office.presentation.user.views.ViewNode;

public class ActionGenerateReport extends Action {
	private NodeLayer nodeLayer;
	private ComponentDocuments componentDocuments;

	public ActionGenerateReport() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	public String execute() {
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String idParent = LibraryRequest.getParameter(Parameter.ID_PARENT, this.request);
		String mode = LibraryRequest.getParameter(Parameter.MODE, this.request);
		String dataSourceTemplate = LibraryRequest.getParameter(Parameter.DATA_SOURCE_TEMPLATE, this.request);
		String nodes = LibraryRequest.getParameter(Parameter.NODES, this.request);
		String nodeTypes = LibraryRequest.getParameter(Parameter.NODE_TYPES, this.request);
		String fromDate = LibraryRequest.getParameter(Parameter.FROM_DATE, this.request);
		String toDate = LibraryRequest.getParameter(Parameter.TO_DATE, this.request);
		ActionLoadNode action;
		Node node, parent;
		ViewNode viewNode;
		String content;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (code == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.GENERATE_REPORT);
		}

		parent = this.nodeLayer.loadNode(idParent);

		if (!this.componentSecurity.canWrite(parent, this.getAccount())) {
			return ErrorCode.WRITE_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.WRITE_NODE_PERMISSIONS);
		}

		node = this.nodeLayer.addNode(code, parent);

		if (!node.isDocument()) {
			this.nodeLayer.deleteNode(node);
			this.nodeLayer.removeNodeFromTrash(node.getId());
			throw new DataException(ErrorCode.NODE_DEFINITION_INCORRECT, Actions.GENERATE_REPORT);
		}

		viewNode = (ViewNode) this.viewsFactory.get(Views.NODE, this.agentRender, this.codeLanguage);
		viewNode.setTarget(node);
		viewNode.setType(ViewType.DOCUMENT);
		viewNode.setMode(dataSourceTemplate);
		viewNode.addParameter(Parameter.NODES, nodes);
		viewNode.addParameter(Parameter.NODE_TYPES, nodeTypes);
		viewNode.addParameter(Parameter.FROM_DATE, fromDate);
		viewNode.addParameter(Parameter.TO_DATE, toDate);
		content = viewNode.execute();

		try {
			this.componentDocuments.updateDocument(node.getId(), content, true);
			this.componentDocuments.consolidateDocument(node, true);
			this.nodeLayer.makeUneditable(node);
		} catch (Exception oException) {
			this.nodeLayer.deleteNode(node);
			this.nodeLayer.removeNodeFromTrash(node.getId());
			throw new DataException(ErrorCode.NODE_UPDATE_DOCUMENT, Actions.GENERATE_REPORT, oException);
		}

		if (mode != null) {
			this.request.setAttribute(Parameter.ID, node.getId());
			this.request.setAttribute(Parameter.MODE, mode);
			action = (ActionLoadNode) this.actionsFactory.get(Actions.LOAD_NODE, this.request, this.response);
			return action.execute();
		}

		return Language.getInstance().getMessage(MessageCode.NODE_ADDED);
	}

}