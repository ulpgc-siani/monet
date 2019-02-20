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
import org.monet.metamodel.CatalogDefinition;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeDataRequest;
import org.monet.space.kernel.model.ReferenceAttribute;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.applications.library.LibraryRequest;

import java.util.HashMap;

public class ActionLoadSetItems extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadSetItems() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String contentType = LibraryRequest.getParameter(Parameter.SET, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		NodeDataRequest dataRequest = new NodeDataRequest();
		HashMap<String, String> parameters;
		int count;
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();
		Dictionary dictionary = Dictionary.getInstance();
		NodeDefinition definition = dictionary.getNodeDefinition(code);
		IndexDefinition indexDefinition = null;

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (contentType == null || id == null || code == null || codeView == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_SET_ITEMS);
		}

		if (definition.isCollection()) {
			String nameReference = ((CollectionDefinition) definition).getIndex().getValue();
			indexDefinition = dictionary.getIndexDefinition(nameReference);
		} else if (definition.isCatalog()) {
			String nameReference = ((CatalogDefinition) definition).getIndex().getValue();
			indexDefinition = dictionary.getIndexDefinition(nameReference);
		} else
			indexDefinition = new DescriptorDefinition();

		parameters = this.getRequestParameters();

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		try {
			dataRequest.setStartPos(Integer.valueOf(parameters.get(Parameter.START)));
		} catch (NumberFormatException oException) {
			dataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			dataRequest.setStartPos(0);
		}

		try {
			dataRequest.setLimit(Integer.valueOf(parameters.get(Parameter.LIMIT)));
		} catch (NumberFormatException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		} catch (ClassCastException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		}

		dataRequest.setCodeDomainNode(code);
		dataRequest.setCodeReference(indexDefinition.getCode());
		dataRequest.setCodeView(codeView);
		dataRequest.setSortsBy(getSortsBy(parameters.get(Parameter.SORTS_BY)));
		dataRequest.setGroupsBy(getGroupsBy(parameters.get(Parameter.GROUPS_BY)));
		dataRequest.setParameters(parameters);

		count = this.nodeLayer.requestNodeSetItemsCount(id, contentType, dataRequest);
		if (count > 0) {
			for (Node node : this.nodeLayer.requestNodeSetItems(id, contentType, dataRequest).values()) {
				JSONObject row = new JSONObject();
				row.put("id", node.getId());
				row.put("type_label", Dictionary.getInstance().getDefinition(node.getCode()).getLabel());
				for (ReferenceAttribute<?> attribute : node.getReference(DescriptorDefinition.CODE).getAttributes().values()) {
					row.put(attribute.getCode(), attribute.getValueAsString());
				}
				rows.add(row);
			}
		}

		result.put("nrows", count);
		result.put("rows", rows);

		return result.toString();
	}

}