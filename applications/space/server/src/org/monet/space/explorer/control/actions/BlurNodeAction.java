package org.monet.space.explorer.control.actions;

import net.minidev.json.JSONObject;
import org.monet.space.explorer.control.dialogs.NodeDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.listeners.ListenerPushService;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.PushClient;

import java.io.IOException;

abstract class BlurNodeAction<Dlg extends NodeDialog> extends Action<Dlg, Display<String>> {

    @Override
    public void execute() throws IOException {
        sendPushMessage(ListenerPushService.PushClientMessages.REMOVE_OBSERVER);
    }

    protected void sendPushMessage(String message) {
        AgentPushService agentPushService = AgentPushService.getInstance();
        PushClient client = agentPushService.getClientView(getAccount().getUser().getId(), idSession, dialog.getInstanceId());
        agentPushService.pushToViewers(client, PushClient.generateViewId(dialog.getNode()), message, serializeObserverInfo(getAccount()));
    }

    private JSONObject serializeObserverInfo(Account account) {
        JSONObject observer = new JSONObject();
        observer.put("id", account.getId());
        observer.put("fullName", account.getUser().getInfo().getFullname());
        observer.put("email", account.getUser().getInfo().getEmail());
        observer.put("photo", account.getUser().getInfo().getPhoto());
        observer.put("entity", dialog.getEntityId());
        return observer;
    }
}
