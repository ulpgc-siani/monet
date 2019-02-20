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

import cotton.signatory.core.XadesSignatureHelper;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentDocuments;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.monet.documents.PresignedDocument;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.wrappers.NodeDocumentWrapper;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.constants.LabelCode;
import org.monet.space.office.core.model.Language;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class ActionPrepareNodeDocumentSignature extends Action {
	NodeLayer nodeLayer;
	ComponentDocuments componentDocuments;

	public ActionPrepareNodeDocumentSignature() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		this.componentDocuments = ComponentDocuments.getInstance();
	}

	private String getFirstRoleLabel(SignatureProperty signatureDefinition) {
		RoleList roleList = this.getAccount().getRoleList();
		org.monet.space.office.core.model.Language language = Language.getInstance();
		Dictionary dictionary = Dictionary.getInstance();
		Role role = null;

		if (roleList.getCount() <= 0)
			return "done";

		for (Ref ref : signatureDefinition.getFor()) {
			String code = dictionary.getDefinitionCode(ref.getValue());
			if (roleList.exist(code))
				role = roleList.get(code);
		}

		if (role == null) role = roleList.get().values().iterator().next();

		return language.getModelResource(dictionary.getDefinition(role.getCode()).getLabel());
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String signatureId = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String signature = LibraryRequest.getParameter(Parameter.SIGNATURE, this.request);
		String reason = LibraryRequest.getParameter(Parameter.REASON, this.request);
		String location = LibraryRequest.getParameter(Parameter.LOCATION, this.request);
		Node node;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.PREPARE_NODE_DOCUMENT_SIGNATURE);

		node = this.nodeLayer.loadNode(id);

		if (node.isEditable()) {
			this.componentDocuments.consolidateDocument(node, false);
			this.nodeLayer.makeUneditable(node);
		}

		if (!this.getAccount().canSign(node))
			throw new DataException(ErrorCode.CANT_SIGN, Actions.PREPARE_NODE_DOCUMENT_SIGNATURE);

		String signatureCode = NodeDocumentWrapper.getSignatureCodeFromId(signatureId);
		int signaturePosition = NodeDocumentWrapper.getSignaturePositionFromId(signatureId);
		Language language = Language.getInstance();
		DocumentDefinition definition = (DocumentDefinition) node.getDefinition();
		SignatureProperty signatureDefinition = definition.getSignature(signatureCode);
		UserInfo info = this.getAccount().getUser().getInfo();
		String firstRole = this.getFirstRoleLabel(signatureDefinition);
		String contact = info.getFullname() + " (" + info.getEmail() + ")" + (!firstRole.isEmpty() ? Language.getInstance().getLabel(LabelCode.WITH_ROLE) + firstRole : "");

		try {
			X509Certificate certificateObject = new XadesSignatureHelper().getCertificate(signature);
			String certificate = new String(Base64.encodeBase64(certificateObject.getEncoded()));

			PresignedDocument presignedData = this.componentDocuments.prepareDocumentSignature(id, certificate, reason, location, contact, signatureId);
			this.nodeLayer.addNodeDocumentSignature(node, signatureCode, signaturePosition, language.getModelResource(signatureDefinition.getLabel()), reason, location, contact, presignedData.getDate());

			JSONObject presignedDataJSON = new JSONObject();
			presignedDataJSON.put("id", presignedData.getSignId());
			presignedDataJSON.put("hash", presignedData.getHash());
			presignedDataJSON.put("certificateSN", certificateObject.getSerialNumber().toString(16));

			return presignedDataJSON.toString();

		} catch (CertificateEncodingException e) {
			throw new DataException(ErrorCode.CANT_SIGN, Actions.PREPARE_NODE_DOCUMENT_SIGNATURE);
		}
	}

}