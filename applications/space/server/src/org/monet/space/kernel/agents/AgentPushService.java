package org.monet.space.kernel.agents;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.listeners.IListenerPushService;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.PushEvent;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.UserPushContext;

import javax.servlet.AsyncContext;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AgentPushService implements IListenerPushService {
	private static AgentPushService oInstance;

	private Map<String, UserPushContext> connectedUsers = new ConcurrentHashMap<>();
	private boolean isPushEnable;

	protected AgentPushService() {
		this.isPushEnable = Configuration.getInstance().isPushEnable();
		AgentPushServiceNotifier.getInstance().register("agentPushService", this);
	}

	public synchronized static AgentPushService getInstance() {
		if (oInstance == null) oInstance = new AgentPushService();
		return oInstance;
	}

	public synchronized void addClient(User user, String sessionId, String clientId, AsyncContext asyncContext, boolean compatibilityMode) {
		if (!this.isPushEnable) return;

		UserPushContext context = this.connectedUsers.get(user.getId());
		if (context == null) {
			context = new UserPushContext(user);
			this.connectedUsers.put(user.getId(), context);
		}
		context.addClient(user, sessionId, clientId, asyncContext, compatibilityMode);
	}

	public synchronized void removeClient(String userId, String key) {
		if (!this.isPushEnable) return;

		UserPushContext context = this.connectedUsers.get(userId);
		if (context != null) {
			context.removeClient(key);
			if (context.getClientCount() == 0)
				this.connectedUsers.remove(userId);
		}
	}

	public synchronized void removeChannels(String userId, String sessionId) {
		UserPushContext context = this.connectedUsers.get(userId);
		if (context != null) {
			context.removeAllClients(sessionId);
			if (context.getClientCount() == 0)
				this.connectedUsers.remove(userId);
		}
	}

	public synchronized void push(String userId, String operation, JSONObject data) {
		if (!this.isPushEnable) return;

		UserPushContext context = this.connectedUsers.get(userId);
		if (context != null) {
			String message = createMessage(operation, data);
			context.push(message);
		}
	}

	public synchronized void pushToViewers(PushClient sender, String viewId, String operation, JSONObject data) {
		if (!this.isPushEnable) return;

		String message = createMessage(operation, data);

		for (UserPushContext context : this.connectedUsers.values()) {
			context.pushToViewers(sender, viewId, message);
		}
	}

	public synchronized List<UserPushContext> getViewObservers(String viewId) {
		ArrayList<UserPushContext> result = new ArrayList<>();

		for (UserPushContext context : this.connectedUsers.values()) {
			if (context.useView(viewId)) result.add(context);
		}

		return result;
	}

	public synchronized void pushBroadcastNotMe(String userId, String operation, JSONObject data) {
		if (!this.isPushEnable) return;

		String message = createMessage(operation, data);
		for (UserPushContext context : this.connectedUsers.values()) {
			if (context.getUserId().equals(userId))
				continue;
			context.push(message);
		}
	}

	public synchronized void pushBroadcast(String operation, JSONObject data) {
		if (!this.isPushEnable) return;

		String message = createMessage(operation, data);
		for (UserPushContext context : this.connectedUsers.values()) {
			context.push(message);
		}
	}

	private String createMessage(String operation, JSONObject data) {
		String message;
		JSONObject pushMessage = new JSONObject();
		pushMessage.put("op", operation);
		pushMessage.put("data", data);
		message = pushMessage.toString();
		return message;
	}

	public synchronized PushClient getClientView(String userId, String sessionId, String clientId) {
		if (!this.isPushEnable) return null;

		UserPushContext context = this.connectedUsers.get(userId);
		if (context != null)
			return context.getClientView(sessionId, clientId);

		return null;
	}

	public synchronized void updateClientView(String userId, String sessionId, String clientId, String viewId) {
		if (!isPushEnable) return;

		UserPushContext context = connectedUsers.get(userId);
		if (context != null)
			context.updateClientView(sessionId, clientId, viewId);
	}

	public synchronized void updateClientViewContext(String userId, String sessionId, String clientId, String viewId, Map<String, String> viewContext) {
		if (!isPushEnable) return;

		UserPushContext context = connectedUsers.get(userId);
		if (context != null)
			context.updateClientViewContext(sessionId, clientId, viewId, viewContext);
	}

	@Override
	public void removeClient(PushEvent event) {
		PushClient pushClient = (PushClient) event.getSender();
		this.removeClient(pushClient.getUserId(), pushClient.getKey());
	}

	public boolean isActive(String viewId) {
		for (UserPushContext context : this.connectedUsers.values()) {
			if (context.isViewing(viewId))
				return true;
		}
		return false;
	}

	public synchronized HashSet<String> getActiveViews() {
		HashSet<String> activeViews = new HashSet<>();

		for (UserPushContext context : this.connectedUsers.values()) {
			activeViews.addAll(context.getViewings());
		}

		return activeViews;
	}

}
