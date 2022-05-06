package org.monet.bpi.java;

import net.minidev.json.JSONObject;
import org.monet.bpi.ClientService;
import org.monet.bpi.MonetLink;
import org.monet.bpi.User;
import org.monet.bpi.types.File;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.*;

import static org.monet.space.kernel.listeners.ListenerPushService.PushClientMessages;

public class ClientServiceImpl extends ClientService {

	public static void init() {
		instance = new ClientServiceImpl();
	}

	@Override
	protected void redirectUserToImpl(MonetLink monetLink) {
		MonetLinkImpl link = ((MonetLinkImpl) monetLink);
		String operation;

		JSONObject data = new JSONObject();
		data.put("Id", link.getId());
		data.put("Mode", link.isEditMode() ? "edit.html?mode=page" : "");

		switch (link.getType()) {
			case Node:
				operation = PushClientMessages.SHOW_NODE;
				break;
			case Task:
				operation = PushClientMessages.SHOW_TASK;
				break;
			case User:
				return;
			default:
				return;
		}

		AgentUserClient.getInstance().sendOperationToUser(Thread.currentThread().getId(), new ClientOperation(operation, data));
		if (operation.equals(PushClientMessages.SHOW_NODE)) showNodeView(link);
	}

	private void showNodeView(MonetLinkImpl link) {
		String view = link.getView();

		if (view == null || view.isEmpty()) return;

		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node = nodeLayer.loadNode(link.getId());

		JSONObject data = new JSONObject();
		data.put("IdMainNode", link.getId());
		data.put("Id", link.getId());
		data.put("IdView", node.getDefinition().getNodeView(view).getCode());

		AgentUserClient.getInstance().sendOperationToUser(Thread.currentThread().getId(), new ClientOperation(PushClientMessages.SHOW_NODE_VIEW, data));
	}

	@Override
	protected void sendMessageToUserImpl(String message) {
		AgentUserClient.getInstance().sendMessageToUser(Thread.currentThread().getId(), message);
	}

	@Override
	protected void sendFileToUserImpl(File file) {
		AgentUserClient.getInstance().sendFileToUser(Thread.currentThread().getId(), file);
	}

	@Override
	protected User discoverUserInSessionImpl() {
		Session session = Context.getInstance().getCurrentSession();
		if (session == null) return null;
		Account account = session.getAccount();
		return account != null ? userOf(account.getUser()) : null;
	}

	private User userOf(final org.monet.space.kernel.model.User user) {
		if (user == null) return null;
		UserImpl result = new UserImpl();
		result.injectUser(user);
		return result;
	}

}
