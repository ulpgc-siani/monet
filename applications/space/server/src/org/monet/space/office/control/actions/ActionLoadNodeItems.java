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
import org.monet.bpi.types.Link;
import org.monet.metamodel.*;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.NodeDataRequest;
import org.monet.space.kernel.model.ReferenceAttribute;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

import java.util.Collection;

public class ActionLoadNodeItems extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodeItems() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		String code = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		int count;
		Dictionary dictionary = Dictionary.getInstance();
		NodeDefinition definition = dictionary.getNodeDefinition(code);
		String nameReference = DescriptorDefinition.CODE;
		IndexDefinition indexDefinition = null;
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (idNode == null || code == null || codeView == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_ITEMS);

		if (definition.isCollection()) {
			CollectionDefinition collectionDefinition = ((CollectionDefinition) definition);
			if (collectionDefinition.getIndex() != null)
				nameReference = collectionDefinition.getIndex().getValue();
		} else if (definition.isCatalog()) {
			CatalogDefinition catalogDefinition = ((CatalogDefinition) definition);
			if (catalogDefinition.getIndex() != null)
				nameReference = catalogDefinition.getIndex().getValue();
		} else
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_ITEMS);

		indexDefinition = dictionary.getIndexDefinition(nameReference);

		NodeDataRequest dataRequest = createNodeDataRequest(null, code);
		dataRequest.setCodeReference(indexDefinition.getCode());
		dataRequest.setCodeView(codeView);

		count = this.nodeLayer.requestNodeListItemsCount(idNode, dataRequest);
		if (count > 0) {
			Collection<Node> values = this.nodeLayer.requestNodeListItems(idNode, dataRequest).values();
			for (Node node : values) {
				JSONObject row = new JSONObject();
				String label = node.getShortLabel();

				row.put("id", node.getId());

				if (label == null || label.trim().isEmpty())
					label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

				for (ReferenceAttribute<?> attribute : node.getReference().getAttributes().values()) {
					String value = attribute.getValueAsString();
					row.put(attribute.getCode(), value);
				}

				row.put("label", node.getLabel());
				for (ReferenceAttribute<?> attribute : node.getReference(indexDefinition.getCode()).getAttributes().values()) {
					String valueAsString = attribute.getValueAsString();
					Object value = attribute.getValue();
					if (value instanceof Link)
						row.put(attribute.getCode() + "_extra", ((Link) value).getId());
					row.put(attribute.getCode(), valueAsString);
				}
				rows.add(row);
			}
		}

		result.put("nrows", count);
		result.put("rows", rows);

		return result.toString();
	}

}