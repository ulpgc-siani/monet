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

import org.jdom.Element;
import org.monet.space.kernel.constants.Strings;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NodeList extends BaseModelList<Node> {
	private NodeLink nodeLink;
	private String nodeId;

	public static final String ITEMS = "Items";

	public NodeList() {
		this.nodeLink = null;
		this.nodeId = Strings.UNDEFINED_ID;
	}

	public NodeList(NodeList oNodeList) {
		this.nodeLink = null;
		this.code = oNodeList.code;
		this.nodeId = oNodeList.nodeId;
		this.items.putAll(oNodeList.items);
		this.codes.putAll(oNodeList.codes);
	}

	public void setNodeLink(NodeLink nodeLink) {
		this.nodeLink = nodeLink;
	}

	public String getIdNode() {
		return this.nodeId;
	}

	public void setIdNode(String idNode) {
		this.nodeId = idNode;
	}

	public MonetHashMap<Node> get() {
		onLoad(this, NodeList.ITEMS);
		return this.items;
	}

	public Map<String, Node> get(String codeReference, String[] codeNodes, int start, int limit) {
		NodeDataRequest dataRequest = new NodeDataRequest();
		dataRequest.setCodeReference(codeReference);
		dataRequest.setCodeNodes(codeNodes);
		dataRequest.setCode(this.getId());
		dataRequest.setStartPos(start);
		dataRequest.setLimit(limit);
		return this.nodeLink.requestNodeListItems(this.nodeId, dataRequest);
	}

	public int getCount(String codeReference, String[] codeNodes) {
		NodeDataRequest dataRequest = new NodeDataRequest();
		dataRequest.setCodeReference(codeReference);
		dataRequest.setCodeNodes(codeNodes);
		return this.nodeLink.requestNodeListItemsCount(this.nodeId, dataRequest);
	}

	public void add(Node node) {
		super.add(node);
	}

	public void delete(String codeId) {
		String id = null;

		if (this.items.containsKey(codeId)) id = codeId;
		if ((id == null) && (this.codes.containsKey(codeId))) id = this.codes.get(codeId);
		if (id == null) return;

		this.items.remove(id);
		this.codes.remove(id);

	}

	public void clear() {
		this.items.clear();
		this.codes.clear();
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "nodelist");
		for (Node node : this.get().values())
			node.serializeToXML(serializer, depth);
		serializer.endTag("", "nodelist");
	}

	@SuppressWarnings("unchecked")
	public void unserializeFromXML(Element nodeList) {
		List<Element> nodes;
		Iterator<Element> iterator;

		if (nodeList == null) return;

		nodes = nodeList.getChildren("node");
		iterator = nodes.iterator();

		this.clear();

		while (iterator.hasNext()) {
			Node node = new Node();
			node.unserializeFromXML(iterator.next());
			if (node.getId() == Strings.UNDEFINED_ID) node.setId(String.valueOf(this.items.size() + 1));
			this.add(node);
		}

	}

}
