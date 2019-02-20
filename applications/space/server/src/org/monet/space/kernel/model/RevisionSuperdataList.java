package org.monet.space.kernel.model;

import java.util.LinkedHashMap;


public class RevisionSuperdataList extends BaseModelList<Revision> {

	public static final String ITEMS = "Items";

	public RevisionSuperdataList() {
//    this.nodeLink = null;
//    this.nodeId = Strings.UNDEFINED_ID; 
	}

	public RevisionSuperdataList(RevisionSuperdataList revisionList) {
//    this.nodeLink = null;
//    this.nodeId = revisionList.nodeId;
		this.code = revisionList.code;
		this.items.putAll(revisionList.items);
		this.codes.putAll(revisionList.codes);
	}


	public LinkedHashMap<String, Node> get(String reference, String[] codeNodes, int startPos, int limit) {
		DataRequest dataRequest = new DataRequest();
		dataRequest.setCode(this.getId());
		dataRequest.setStartPos(startPos);
		dataRequest.setLimit(limit);
		//return this.nodeLink.requestNodeListItems(this.nodeId, reference, codeNodes, dataRequest);
		return null;
	}


}
