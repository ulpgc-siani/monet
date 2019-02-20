package org.monet.mobile.service.requests;

import org.monet.mobile.model.ChatItem;
import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

import java.util.List;

public class SyncChatsRequest extends Request {

	public long SyncMark;
	public List<ChatItem> ChatItems;

	public SyncChatsRequest() {
		super(ActionCode.SyncChats);
	}

	public SyncChatsRequest(List<ChatItem> chatItems, long syncMark) {
		super(ActionCode.SyncChats);
		this.ChatItems = chatItems;
		this.SyncMark = syncMark;
	}

}
