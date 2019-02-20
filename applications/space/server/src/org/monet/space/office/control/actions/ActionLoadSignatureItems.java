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
import net.minidev.json.JSONObject;
import org.monet.metamodel.DocumentDefinition;
import org.monet.metamodel.DocumentDefinitionBase.SignaturesProperty.SignatureProperty;
import org.monet.metamodel.RoleDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.wrappers.NodeDocumentWrapper;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.monet.space.kernel.model.wrappers.NodeDocumentWrapper.*;

public class ActionLoadSignatureItems extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadSignatureItems() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();
		Node node;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_SIGNATURE_ITEMS);
		}

		node = this.nodeLayer.loadNode(id);

		if (!this.componentSecurity.canRead(node, this.getAccount())) {
			return ErrorCode.READ_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.READ_NODE_PERMISSIONS);
		}

		if (!node.isDocument())
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_SIGNATURE_ITEMS);

		DocumentDefinition documentDefinition = (DocumentDefinition) node.getDefinition();
		Map<String, SignatureProperty> signaturesDefinitionMap = new HashMap<>();

		if (documentDefinition.getSignatures() != null)
			signaturesDefinitionMap = documentDefinition.getSignatures().getSignatureMap();

		for (SignatureProperty signatureDefinition : signaturesDefinitionMap.values())
			addSignatures(rows, signatureDefinition, node);

		result.put("nrows", rows.size());
		result.put("rows", rows);

		return result.toString();
	}

	public void addSignatures(JSONArray jsonSignatures, final SignatureProperty signatureDefinition, Node node) {
		NodeDocumentWrapper wrapper = new NodeDocumentWrapper(node);
		String signatureDefinitionCode = signatureDefinition.getCode();
		int countSignatures = wrapper.getCountSignaturesOfType(signatureDefinition);

		for (int pos = 0; pos < countSignatures; pos++) {
			Signature signature = wrapper.getSignature(signatureDefinitionCode, pos);
			JSONObject jsonSignature = new JSONObject();

			addSignatureProperties(jsonSignature, node, signature, pos, signatureDefinition);
			addSignatureState(jsonSignature, node, signature, pos, signatureDefinition);
			addSignatureRoles(jsonSignature, node, signature, pos, signatureDefinition);
			addSignatureUsers(jsonSignature, node, signature, pos, signatureDefinition);

			jsonSignatures.add(jsonSignature);
		}
	}

	private boolean isSignatureDone(Node node, Signature signature, int signaturePos, SignatureProperty signatureDefinition) {
		return signature != null;
	}

	private boolean isSignatureDisabled(Node node, Signature signature, int signaturePos, SignatureProperty signatureDefinition) {
		return new NodeDocumentWrapper(node).isSignatureDisabled(signatureDefinition.getCode(), signaturePos);
	}

	private void addSignatureState(JSONObject jsonSignature, Node node, Signature signature, int signaturePos, SignatureProperty signatureDefinition) {

		if (isSignatureDisabled(node, signature, signaturePos, signatureDefinition)) {
			jsonSignature.put("state", "disabled");
			return;
		}

		if (isSignatureDone(node, signature, signaturePos, signatureDefinition)) {
			jsonSignature.put("state", "signed");
			return;
		}

		if (!hasRole(signatureDefinition)) {
			jsonSignature.put("state", "waiting");
			return;
		}

		if (signatureDefinition.getAfter() != null) {
			DocumentDefinition documentDefinition = (DocumentDefinition) node.getDefinition();
			Map<String, SignatureProperty> signaturesDefinitionMap = new HashMap<String, SignatureProperty>();

			if (documentDefinition.getSignatures() != null)
				signaturesDefinitionMap = documentDefinition.getSignatures().getSignatureMap();

			SignatureProperty precedenceSignatureDefinition = signaturesDefinitionMap.get(signatureDefinition.getAfter().getValue());
			Attribute precedence = node.getAttribute(precedenceSignatureDefinition.getCode());

			if (precedence != null)
				jsonSignature.put("state", "pending");
			else
				jsonSignature.put("state", "disabled");

			return;
		}

		jsonSignature.put("state", "pending");
	}

	private void addSignatureProperties(JSONObject jsonSignature, Node node, Signature signature, int signaturePos, SignatureProperty signatureDefinition) {
		String code = String.format(SIGNATURE_ID_PATTERN, signatureDefinition.getCode(), signaturePos);

		jsonSignature.put("label", signatureDefinition.getLabel());
		jsonSignature.put("id", code);
		jsonSignature.put("code", code);

		if (isSignatureDone(node, signature, signaturePos, signatureDefinition)) {
			jsonSignature.put("valid", signature.isValid() ? "1" : "0");
			jsonSignature.put("reason", signature.getReason());
			jsonSignature.put("location", signature.getLocation());
			jsonSignature.put("contact", signature.getContact());
			jsonSignature.put("details", signature.getDetails());
			jsonSignature.put("date", LibraryDate.getDateAndTimeString(new Date(signature.getDate()), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
		} else {
			jsonSignature.put("valid", "0");
			jsonSignature.put("reason", "");
			jsonSignature.put("location", "");
			jsonSignature.put("contact", "");
			jsonSignature.put("date", "");
			jsonSignature.put("details", "");
		}
	}

	private void addSignatureRoles(JSONObject jsonSignature, Node node, Signature signature, int signaturePos, SignatureProperty signatureDefinition) {
		ArrayList<String> roles = new ArrayList<>();

		for (Ref roleRef : signatureDefinition.getFor()) {
			RoleDefinition roleDefinition = Dictionary.getInstance().getRoleDefinition(roleRef.getValue());
			if (roleDefinition.isDisabled()) continue;
			roles.add(Language.getInstance().getModelResource(roleDefinition.getLabel()));
		}

		jsonSignature.put("roles", LibraryArray.implode(roles, ", "));
	}

	private void addSignatureUsers(JSONObject jsonSignature, Node node, Signature signature, int signaturePos, SignatureProperty signatureDefinition) {
		Map<String, Map<Integer, NodeDocumentWrapper.SignatureUserRestriction>> signaturesMap = new NodeDocumentWrapper(node).getSignatureUserRestrictions();
		String code = signatureDefinition.getCode();
		SignatureUserRestriction signatureUserRestrictionWrapper = null;

		if (signaturesMap.containsKey(code) && signaturesMap.get(code).containsKey(signaturePos))
			signatureUserRestrictionWrapper = signaturesMap.get(code).get(signaturePos);

		jsonSignature.put("users", signatureUserRestrictionWrapper != null ? LibraryArray.implode(signatureUserRestrictionWrapper.getUserFullNames(), ", ") : "");
		jsonSignature.put("users_ids", signatureUserRestrictionWrapper != null ? "#" + LibraryArray.implode(signatureUserRestrictionWrapper.getUserIds(), "#") + "#" : "");
		jsonSignature.put("users_count", signatureUserRestrictionWrapper != null ? signatureUserRestrictionWrapper.getUserFullNames().size() : 0);
	}

}