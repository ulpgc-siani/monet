package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.model.ChatItem;
import org.monet.mobile.service.requests.SyncChatsRequest;
import org.monet.mobile.service.results.SyncChatsResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.ChatItem.Type;

import java.util.ArrayList;
import java.util.Date;

public class ActionDoSyncChats extends AuthenticatedTypedAction<SyncChatsRequest, SyncChatsResult> {

	TaskLayer taskLayer;

	public ActionDoSyncChats() {
		super(SyncChatsRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
	}

	@Override
	protected SyncChatsResult onExecute(SyncChatsRequest request) throws ActionException {
		SyncChatsResult result = new SyncChatsResult();

		Account account = this.getAccount();

		result.Chats = new ArrayList<ChatItem>();
		for (org.monet.space.kernel.model.ChatItem chatItem : taskLayer.loadJobNewChatItems(account.getId(), request.SyncMark)) {
			result.Chats.add(new ChatItem(chatItem.getTaskId(), chatItem.getMessage(), chatItem.getInternalCreateDate().getTime(), chatItem.getType() == Type.in));
		}

		for (ChatItem item : request.ChatItems) {
			if (!item.IsOut)
				continue;
			org.monet.space.kernel.model.ChatItem chatItem = new org.monet.space.kernel.model.ChatItem();
			chatItem.setMessage(item.Message);
			chatItem.setType(Type.in);
			chatItem.setSent(true);
			chatItem.setCreateDate(new Date());
			this.taskLayer.addJobChatListItem(item.ServerId, chatItem);
		}

		result.SyncMark = new Date().getTime();

		return result;
	}

}
