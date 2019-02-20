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

package org.monet.space.kernel.model;

import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.FilterProperty;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;

import java.util.*;

public class NodeProvider implements NodeLink {
	private NodeLayer nodeLayer;

	private static NodeProvider instance;

	private NodeProvider() {
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public synchronized static NodeProvider getInstance() {
		if (instance == null) instance = new NodeProvider();
		return instance;
	}

	@Override
	public Node loadNode(String id) {
		return this.nodeLayer.loadNode(id);
	}

	@Override
	public Map<String, Node> requestNodeListItems(String nodeId, NodeDataRequest dataRequest) {
		return this.nodeLayer.requestNodeListItems(nodeId, dataRequest);
	}

	@Override
	public int requestNodeListItemsCount(String nodeId, NodeDataRequest dataRequest) {
		return this.nodeLayer.requestNodeListItemsCount(nodeId, dataRequest);
	}

	@Override
	public Map<String, Node> requestNodeSetItems(String nodeId, String contentType, NodeDataRequest dataRequest) {
		return this.nodeLayer.requestNodeSetItems(nodeId, contentType, dataRequest);
	}

	@Override
	public int requestNodeSetItemsCount(String nodeId, String contentType, NodeDataRequest dataRequest) {
		return this.nodeLayer.requestNodeSetItemsCount(nodeId, contentType, dataRequest);
	}

	@Override
	public NodeList searchNodeListItems(String nodeId, SearchRequest searchRequest) {
		return this.nodeLayer.search(nodeId, searchRequest);
	}

	@Override
	public int requestNodeRevisionItemsCount(String nodeId, DataRequest dataRequest) {
		return this.nodeLayer.requestRevisionListItemsCount(nodeId, dataRequest);
	}

	@Override
	public List<String> loadReferenceAttributeValues(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		return this.nodeLayer.loadReferenceAttributeValues(ownerId, codeReference, codeAttribute, filterNodes, filterAttributesDefinition);
	}

	@Override
	public Map<String, Integer> loadReferenceAttributeValuesCount(String ownerId, String codeReference, String codeAttribute, List<String> filterNodes, List<FilterProperty> filterAttributesDefinition) {
		return this.nodeLayer.loadReferenceAttributeValuesCount(ownerId, codeReference, codeAttribute, filterNodes, filterAttributesDefinition);
	}

}