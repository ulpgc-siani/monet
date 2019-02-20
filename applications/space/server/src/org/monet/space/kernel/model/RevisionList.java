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

import org.monet.space.kernel.constants.Strings;

import java.util.LinkedHashMap;
import java.util.Map;

public class RevisionList extends BaseModelList<Revision> {
	private NodeLink nodeLink;
	private String nodeId;
	private RevisionLink revisionLink;

	public static final String ITEMS = "Items";

	public RevisionList() {
		this.nodeLink = null;
		this.nodeId = Strings.UNDEFINED_ID;
	}

	public RevisionList(RevisionList oNodeList) {
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

	public MonetHashMap<Revision> get() {
		onLoad(this, RevisionList.ITEMS);
		return this.items;
	}

	public void setRevisionLink(RevisionLink revisionLink) {
		this.revisionLink = revisionLink;
	}

	public RevisionLink getRevisionLink() {
		onLoad(this, Revision.REVISION_LINK);
		return this.revisionLink;
	}

	public Map<String, Revision> getRevisionListItems(int startPos, int limit) {
		DataRequest oDataRequest = new DataRequest();
		oDataRequest.setCode(this.nodeId);
		oDataRequest.setStartPos(startPos);
		oDataRequest.setLimit(limit);

		return (revisionLink != null) ? revisionLink.requestRevisionListItems(this.nodeId, oDataRequest) : new LinkedHashMap<String, Revision>();
	}

	public int getCount(String reference, String[] codeNodes) {
		NodeDataRequest dataRequest = new NodeDataRequest();
		dataRequest.setCodeReference(reference);
		dataRequest.setCodeNodes(codeNodes);
		return this.nodeLink.requestNodeListItemsCount(this.nodeId, dataRequest);
	}

	public void add(Revision node) {
		super.add(node);
	}

	public void delete(String codeId) {
		String id = null;

		if (this.items.containsKey(codeId)) id = codeId;
		if ((id == null) && (this.codes.containsKey(codeId))) id = (String) this.codes.get(codeId);
		if (id == null) return;

		this.items.remove(id);
		this.codes.remove(id);

	}

	public void clear() {
		this.items.clear();
		this.codes.clear();
	}

//  public StringBuffer serializeToXML() {
//    return this.serializeToXML(false);
//  }

//  public StringBuffer serializeToXML(boolean addHeader) {
//    Iterator<String> iterator = this.items.keySet().iterator();
//    StringBuffer result = new StringBuffer();
//
//    if (addHeader) result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//    
//    result.append("<nodelist>");
//    while(iterator.hasNext()) {
//      String id = iterator.next();
//      Revision node = this.items.get(id);
//      result.append(node.serializeToXML());
//    }
//    result.append("</nodelist>");
//    
//    return result;
//  }

//  @SuppressWarnings("unchecked")
//  public void unserializeFromXML(Element nodeList) {
//    List<Element> nodes;
//    Iterator<Element> iterator;
//    
//    if (nodeList == null) return;
//    
//    nodes = nodeList.getChildren("node");
//    iterator = nodes.iterator();
//
//    this.clear();
//
//    while (iterator.hasNext()) {
//      Revision node = new Revision();
//      node.unserializeFromXML(iterator.next());
//      if (node.getId() == Strings.UNDEFINED_ID) node.setId(String.valueOf(this.items.size()+1)); 
//      this.add(node);
//    }
//    
//  }

}
