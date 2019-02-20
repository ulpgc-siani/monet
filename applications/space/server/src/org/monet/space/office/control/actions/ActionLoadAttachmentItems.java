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
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty.AttachmentsProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty.ShowProperty.AttachmentsProperty.FieldNodeProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Attachment;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeDataRequest;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ActionLoadAttachmentItems extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadAttachmentItems() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String view = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		NodeDataRequest dataRequest = new NodeDataRequest();
		HashMap<String, String> parameters;
		int count;
		JSONObject result = new JSONObject();
		JSONArray categories = new JSONArray(), documents = new JSONArray();
		HashSet<String> categoriesSet = new HashSet<String>();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (id == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_ATTACHMENT_ITEMS);
		}

		parameters = this.getRequestParameters();

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		dataRequest.setParameters(parameters);
		dataRequest.setCode(code);
		dataRequest.setCodeView(view);

		Node node = this.nodeLayer.loadNode(id);
		FormDefinition definition = (FormDefinition) node.getDefinition();

		count = this.nodeLayer.requestNodeAttachmentItemsCount(id, dataRequest);
		if (count > 0) {
			List<Attachment> attachments = this.nodeLayer.requestNodeAttachmentItems(id, dataRequest);

			for (Attachment attachment : attachments) {
				if (!categoriesSet.contains(attachment.getTargetCode()))
					categoriesSet.add(attachment.getTargetCode());
			}

			FormViewProperty viewDefinition = (FormViewProperty) definition.getNodeView(view);
			AttachmentsProperty attachmentsDefinition = viewDefinition.getShow().getAttachments();

			for (FieldNodeProperty fieldNodeDefinition : attachmentsDefinition.getFieldNodeList())
				this.fill(categories, documents, categoriesSet, definition, attachments, fieldNodeDefinition.getField());

			for (Ref attachment : attachmentsDefinition.getFieldFile())
				this.fill(categories, documents, categoriesSet, definition, attachments, attachment);

		}


		result.put("categories", categories);
		result.put("documents", documents);

		return result.toString();
	}

	private void fill(JSONArray categories, JSONArray documents, HashSet<String> categoriesSet, FormDefinition definition, List<Attachment> attachments, Ref attachment) {
		FieldProperty fieldDefinition = definition.getField(attachment.getValue());

		if (!categoriesSet.contains(fieldDefinition.getCode()))
			return;

		JSONObject jsonCategory = new JSONObject();
		jsonCategory.put("code", fieldDefinition.getCode());
		jsonCategory.put("label", fieldDefinition.getLabel());
		jsonCategory.put("isMultiple", fieldDefinition.isMultiple());
		categories.add(jsonCategory);

		for (Attachment currentAttachment : attachments) {
			if (!currentAttachment.getTargetCode().equals(fieldDefinition.getCode())) continue;
			JSONObject jsonAttachment = currentAttachment.toJson();
			jsonAttachment.put("id", currentAttachment.getTargetId());
			jsonAttachment.put("label", currentAttachment.getData());
			jsonAttachment.put("path", definition.getFieldPath(currentAttachment.getTargetCode()));
			jsonAttachment.put("sourceId", currentAttachment.getSourceId());
			jsonAttachment.put("targetId", currentAttachment.getTargetId());
			jsonAttachment.put("targetType", currentAttachment.getTargetType());
			jsonAttachment.put("targetCode", currentAttachment.getTargetCode());
			documents.add(jsonAttachment);
		}

	}

}