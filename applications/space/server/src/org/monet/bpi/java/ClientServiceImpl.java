package org.monet.bpi.java;

import net.minidev.json.JSONObject;
import org.monet.bpi.ClientService;
import org.monet.bpi.MonetLink;
import org.monet.bpi.User;
import org.monet.bpi.types.File;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.ClientOperation;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Session;

import static org.monet.space.kernel.listeners.ListenerPushService.PushClientMessages;

public class ClientServiceImpl extends ClientService {

	public static void init() {
		instance = new ClientServiceImpl();
	}

	@Override
	protected void redirectUserToImpl(MonetLink monetLink) {
		MonetLinkImpl link = ((MonetLinkImpl) monetLink);
		String operation = null;

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

		if (operation != null)
			AgentUserClient.getInstance().sendOperationToUser(Thread.currentThread().getId(), new ClientOperation(operation, data));
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

		return new User() {
			@Override
			public String getId() {
				return user.getId();
			}

			@Override
			public String getName() {
				return user.getName();
			}

			@Override
			public String getEmail() {
				return user.getInfo().getEmail();
			}

			@Override
			public String getFullName() {
				return user.getInfo().getFullname();
			}
		};
	}
}
