package org.monet.space.kernel.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.List;

public class Chat extends BaseObject {
	private String orderId;
	private ChatLink chatLink;

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public String getIdOrder() {
		return this.orderId;
	}

	public void setIdOrder(String idOrder) {
		this.orderId = idOrder;
	}

	public void setChatLink(ChatLink link) {
		this.chatLink = link;
	}

	public List<ChatItem> getItems(int start, int limit) {
		DataRequest dataRequest = new DataRequest();
		dataRequest.setStartPos(start);
		dataRequest.setLimit(limit);
		return this.chatLink.requestChatListItems(this.orderId, dataRequest);
	}

	public int getItemsCount() {
		return this.chatLink.requestChatListItemsCount(this.orderId);
	}

}
