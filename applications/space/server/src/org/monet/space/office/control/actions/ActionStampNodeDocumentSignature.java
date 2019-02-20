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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.wrappers.NodeDocumentWrapper;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.MessageCode;
import org.monet.space.office.core.model.Language;

public class ActionStampNodeDocumentSignature extends Action {
	NodeLayer nodeLayer;
	ComponentDocuments componentDocuments;

	public ActionStampNodeDocumentSignature() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String signatureId = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String signId = LibraryRequest.getParameter(Parameter.SIGNATURE_ID, this.request);
		String signature = LibraryRequest.getParameter(Parameter.SIGNATURE, this.request);
		Node node;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.STAMP_NODE_DOCUMENT_SIGNATURE);
		}

		node = this.nodeLayer.loadNode(id);

		if (!node.isLocked())
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.STAMP_NODE_DOCUMENT_SIGNATURE);

		NodeDocumentWrapper wrapper = new NodeDocumentWrapper(node);
		String signatureCode = NodeDocumentWrapper.getSignatureCodeFromId(signatureId);
		int signaturePosition = NodeDocumentWrapper.getSignaturePositionFromId(signatureId);

		this.nodeLayer.saveNode(node);
		nodeLayer.stampNodeDocumentSignature(node, signId, signature, getAccount().getUser());
		wrapper.markSignatureAsValid(signatureCode, signaturePosition, signature);

		return Language.getInstance().getMessage(MessageCode.NODE_DOCUMENT_SIGNATURE_STAMPED);
	}

}