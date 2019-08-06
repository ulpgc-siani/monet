package org.monet.space.kernel.listeners;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.news.Post;
import org.monet.space.kernel.model.news.PostComment;

public class ListenerPushService extends Listener {

	private AgentPushService agentPushService = AgentPushService.getInstance();

	@Override
	public void accountModified(MonetEvent event) {
		Account account = (Account) event.getSender();
		JSONObject jsonObject = new JSONObject();
		String id = account.getUser().getId();
		UserInfo info = account.getUser().getInfo();

		jsonObject.put("id", id);
		jsonObject.put("fullName", info.getFullname());
		jsonObject.put("email", info.getEmail());
		jsonObject.put("photo", info.getPhoto());

		this.agentPushService.push(id, PushClientMessages.UPDATE_ACCOUNT, jsonObject);
	}

	@Override
	public void notificationCreated(MonetEvent event) {
		Notification notification = (Notification) event.getSender();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("notification", notification.toJson());
		this.agentPushService.push(notification.getUserId(), PushClientMessages.NEW_NOTIFICATION, jsonObject);
	}

	@Override
	public void notificationPriorized(MonetEvent event) {
		Notification notification = (Notification) event.getSender();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("notification", notification.toJson());
		this.agentPushService.push(notification.getUserId(), PushClientMessages.NEW_NOTIFICATION, jsonObject);
	}

	@Override
	public void nodeSaved(MonetEvent event) {
		Node node = (Node) event.getSender();

		JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("type", "node");
		jsonInfo.put("targetId", node.getId());
		jsonInfo.put("data", null);

		this.agentPushService.pushToViewers(null, PushClient.generateViewId(node), PushClientMessages.UPDATE_VIEW, jsonInfo);
	}

	@Override
	public void nodeAddedToCollection(MonetEvent event) {
		Node parent = event.getParameter(MonetEvent.PARAMETER_COLLECTION);

		JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("type", "node");
		jsonInfo.put("targetId", parent.getId());
		jsonInfo.put("data", null);

		this.agentPushService.pushToViewers(null, PushClient.generateViewId(parent), PushClientMessages.UPDATE_VIEW, jsonInfo);
	}

	@Override
	public void nodeRemovedFromCollection(MonetEvent event) {
		Node parent = event.getParameter(MonetEvent.PARAMETER_COLLECTION);

		JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("type", "node");
		jsonInfo.put("targetId", parent.getId());
		jsonInfo.put("data", null);

		this.agentPushService.pushToViewers(null, PushClient.generateViewId(parent), PushClientMessages.UPDATE_VIEW, jsonInfo);
	}

	@Override
	public void postCreated(MonetEvent event) {
		Post post = (Post) event.getSender();

		JSONObject jsonInfo = new JSONObject();

		if (post.getWallUserId() == null)
			if (post.getType() == Post.USER_POST)
				this.agentPushService.pushBroadcastNotMe(post.getTarget() != null ? post.getTarget().getId() : null, PushClientMessages.UPDATE_NEWS, jsonInfo);
			else
				this.agentPushService.pushBroadcast(PushClientMessages.UPDATE_NEWS, jsonInfo);
		else
			this.agentPushService.push(post.getWallUserId(), PushClientMessages.UPDATE_NEWS, jsonInfo);
	}

	@Override
	public void postCommentCreated(MonetEvent event) {
		Post post = event.getParameter(MonetEvent.PARAMETER_POST);
		PostComment comment = (PostComment) event.getSender();

		JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("notification", post.toJson());

		if (post.getWallUserId() == null)
			agentPushService.pushBroadcastNotMe(comment.getAuthorId(), PushClientMessages.UPDATE_NEWS, jsonInfo);
		else
			agentPushService.push(post.getWallUserId(), PushClientMessages.UPDATE_NEWS, jsonInfo);

	}

	@Override
	public void taskStateUpdated(MonetEvent event) {
        JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("task", ((Task) event.getSender()).getId());
		agentPushService.pushBroadcast(PushClientMessages.UPDATE_TASK_STATE, jsonInfo);
	}

	@Override
	public void taskAssigned(MonetEvent event) {
        JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("task", ((Task) event.getSender()).getId());
		agentPushService.pushBroadcast(PushClientMessages.UPDATE_TASK_OWNER, jsonInfo);
	}

	@Override
	public void taskUnAssigned(MonetEvent event) {
        JSONObject jsonInfo = new JSONObject();
		jsonInfo.put("task", ((Task) event.getSender()).getId());
		agentPushService.pushBroadcast(PushClientMessages.UPDATE_TASK_OWNER, jsonInfo);
	}

	@Override
	public void taskOrderChatMessageReceived(MonetEvent event) {
		String taskId = (String) event.getSender();
		String providerCode = event.getParameter(MonetEvent.PARAMETER_CODE);
		String orderId = event.getParameter(MonetEvent.PARAMETER_ORDER_ID);
		JSONObject jsonInfo = new JSONObject();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		if (orderId == null) {
			TaskOrder taskOrder = taskLayer.loadTaskOrderByCode(taskId, providerCode);
			orderId = taskOrder.getId();
		}

		jsonInfo.put("task", taskId);
		jsonInfo.put("order", orderId);
		agentPushService.pushBroadcast(PushClientMessages.REFRESH_TASK_ORDER_CHAT, jsonInfo);
	}

	@Override
	public void taskOrderChatMessageSent(MonetEvent event) {
		String taskId = (String) event.getSender();
		String orderId = event.getParameter(MonetEvent.PARAMETER_ORDER_ID);
		String chatMessageId = event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		JSONObject jsonInfo = new JSONObject();

		jsonInfo.put("task", taskId);
		jsonInfo.put("order", orderId);
		jsonInfo.put("message", chatMessageId);
		this.agentPushService.pushBroadcast(PushClientMessages.REFRESH_TASK_ORDER_CHAT, jsonInfo);
	}

	public interface PushClientMessages {
		String NEW_NOTIFICATION = "newnotification";
		String UPDATE_VIEW = "updateview";
		String ADD_FIELD = "addfield";
        String DELETE_FIELD = "deletefield";
		String UPDATE_FIELD = "updatefield";
		String UPDATE_NEWS = "updatenews";
		String ADD_OBSERVER = "addobserver";
		String REFRESH_OBSERVER = "refreshobserver";
		String REMOVE_OBSERVER = "removeobserver";
		String BLUR_NODE_FIELD = "blurnodefield";
		String BLUR_NODE = "blurnode";
		String SHOW_TASK = "showtask";
		String SHOW_NODE = "shownode";
		String SHOW_NODE_VIEW = "shownodeview";
		String UPDATE_NODE_STATE = "updatenodestate";
		String UPDATE_TASK_STATE = "updatetaskstate";
		String UPDATE_TASK_OWNER = "updatetaskowner";
		String REFRESH_TASK_ORDER_CHAT = "refreshtaskorderchat";
		String UPDATE_ACCOUNT = "updateaccount";
		String DOWNLOAD_PRINTED_NODE = "downloadprintednode";
    }
}
