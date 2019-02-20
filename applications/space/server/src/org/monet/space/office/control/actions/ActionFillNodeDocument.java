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

import org.monet.bpi.java.ExporterImpl;
import org.monet.bpi.java.NodeDocumentImpl;
import org.monet.bpi.java.NodeImpl;
import org.monet.metamodel.ExporterDefinition;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.NodeAccessException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionFillNodeDocument extends Action {
	private NodeLayer nodeLayer;

	public ActionFillNodeDocument() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String nodeId = LibraryRequest.getParameter(Parameter.ID, this.request);
		String exporterCode = LibraryRequest.getParameter(Parameter.EXPORTER, this.request).toLowerCase();
		String scopeId = LibraryRequest.getParameter(Parameter.SCOPE, this.request);

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (nodeId == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.FILL_NODE_DOCUMENT);

		if (exporterCode != null && exporterCode.isEmpty())
			return Language.getInstance().getMessage(MessageCode.NODE_FILLED);

		if (scopeId != null && scopeId.isEmpty())
			return Language.getInstance().getMessage(MessageCode.NODE_FILLED);

		Node node = this.nodeLayer.loadNode(nodeId);
		if (!this.componentSecurity.canWrite(node, this.getAccount()))
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, nodeId);

		Node scopeNode = this.nodeLayer.loadNode(scopeId);
		if (!this.componentSecurity.canRead(node, this.getAccount()))
			throw new NodeAccessException(org.monet.space.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, nodeId);

		ExporterDefinition definition = Dictionary.getInstance().getExporterDefinition(exporterCode);
		BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
		ExporterImpl bpiExporter = (ExporterImpl) bpiClassLocator.instantiateBehaviour(definition);
		NodeDocumentImpl destination = (NodeDocumentImpl) bpiClassLocator.instantiateBehaviour(node.getDefinition());
		NodeImpl scope = (NodeImpl) bpiClassLocator.instantiateBehaviour(scopeNode.getDefinition());

		destination.injectNode(node);
		scope.injectNode(scopeNode);

		bpiExporter.injectScope(scope);
		bpiExporter.destination = destination;
		bpiExporter.execute();

		if (node.getDefinition().isReadonly())
			destination.consolidate();

		return Language.getInstance().getMessage(MessageCode.NODE_FILLED);
	}

}