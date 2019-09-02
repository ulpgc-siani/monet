package org.monet.space.kernel.model;

import javax.servlet.AsyncContext;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserPushContext {
    private final User user;
	private Map<String, PushClient> clients;
	private Map<String, List<PushClient>> clientsByView;
	private Map<String, List<String>> clientIds;

	public UserPushContext(User user) {
        this.user = user;
		this.clients = new ConcurrentHashMap<>();
		this.clientsByView = new ConcurrentHashMap<>();
		this.clientIds = new ConcurrentHashMap<>();
	}

	public String getUserId() {
		return user.getId();
	}

	public String getUserFullname() {
		return user.getInfo().getFullname();
	}

    public String getUserEmail() {
        return user.getInfo().getEmail();
    }

    public String getUserPhoto() {
        return user.getInfo().getPhoto();
    }

	public void addClient(User user, String sessionId, String clientId, AsyncContext asyncContext, boolean compatibilityMode) {
		String key = PushClient.generateKey(sessionId, clientId);
		PushClient client = this.clients.get(key);
		if (client == null) {
			client = new PushClient(clientId, sessionId, user.getId());
			this.clients.put(key, client);
			List<String> ids = this.clientIds.get(sessionId);
			if (ids == null) {
				ids = new ArrayList<>();
				this.clientIds.put(sessionId, ids);
			}
			ids.add(clientId);
		}
		client.refreshContext(asyncContext, compatibilityMode);
	}

	public int getClientCount() {
		return this.clients.size();
	}

	public void removeClient(String key) {
		PushClient client = this.clients.get(key);
		if (client != null) {
			client.destroy();
			this.removeViewer(client);
			this.clients.remove(key);
			this.clientIds.get(client.getSessionId()).remove(client.getId());
		}
	}

	public void removeAllClients(String sessionId) {
		List<String> sessionClientIds = this.clientIds.get(sessionId);
		for (String clientId : sessionClientIds) {
			String key = sessionId + clientId;
			PushClient client = this.clients.get(key);
			if (client != null) {
				client.destroy();
				this.removeViewer(client);
				this.clients.remove(key);
			}
		}
		this.clientIds.remove(sessionId);
	}

	public PushClient getClientView(String sessionId, String clientId) {
        return clients.get(sessionId + clientId);
	}

	public void updateClientView(String sessionId, String clientId, String viewId) {
        PushClient client = this.clients.get(sessionId + clientId);

		if (client == null) return;

		List<PushClient> clients = this.clientsByView.get(viewId);
		if (clients == null) {
			clients = new ArrayList<>();
			this.clientsByView.put(viewId, clients);
		}
		client.setViewId(viewId);
		client.setViewContext(new HashMap<String, String>());
		clients.add(client);

	}

	public void updateClientViewContext(String sessionId, String clientId, String viewId, Map<String, String> context) {
        PushClient client = clients.get(sessionId + clientId);
		if (client != null)
			client.setViewContext(context);
	}

	private void removeViewer(PushClient client) {
		String viewId = client.getViewId();
		if (viewId != null)
			this.clientsByView.get(viewId).remove(client);
	}

	public void push(String message) {
		for (PushClient client : clients.values())
			client.push(message);
	}

	public synchronized void pushToViewers(PushClient sender, String viewId, String message) {
		if (viewId == null) return;
		List<PushClient> clients = new ArrayList<>(this.clientsByView.get(viewId));
		if (clients == null) return;
        for (PushClient client : clients)
            if (sender != client)
                client.push(message);
	}

	public boolean useView(String viewId) {
        List<PushClient> clients = this.clientsByView.get(viewId);
        return clients != null && clients.size() > 0;
    }

	public List<PushClient> getClientsWithView(String viewId) {
		List<PushClient> clients = this.clientsByView.get(viewId);
		if (clients == null) return new ArrayList<>();
		return clients;
	}

	public boolean isViewing(String viewId) {
		List<PushClient> clients = this.clientsByView.get(viewId);
        return clients != null && clients.size() > 0;
    }

	public Collection<String> getViewings() {
		return this.clientsByView.keySet();
	}
}

  