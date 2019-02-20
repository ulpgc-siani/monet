package org.monet.space.explorer.control.actions;

import net.minidev.json.JSONObject;
import org.monet.space.explorer.control.dialogs.NodeFieldDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.listeners.ListenerPushService;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.PushClient;

import java.io.IOException;
import java.util.Map;

public class FocusNodeFieldAction extends Action<NodeFieldDialog, Display<String>> {

    @Override
    public void execute() throws IOException {
        notifyFocusNodeField(getAccount().getId(), serializeObserverInfo(getAccount()));
        display.write("OK");
    }

    private void notifyFocusNodeField(String accountId, JSONObject observer) {
        PushClient client = AgentPushService.getInstance().getClientView(accountId, idSession, dialog.getInstanceId());
        if (client != null) {
            AgentPushService.getInstance().updateClientViewContext(accountId, idSession, dialog.getInstanceId(), PushClient.generateViewId(dialog.getNode()), context(client));
            pushToViewers(client, ListenerPushService.PushClientMessages.REFRESH_OBSERVER, observer);
        } else
            pushToViewers(null, ListenerPushService.PushClientMessages.ADD_OBSERVER, observer);
    }

    private Map<String, String> context(PushClient client) {
        Map<String, String> context = client.getViewContext();
        context.put("field", dialog.getFieldPath());
        return context;
    }

    private void pushToViewers(PushClient client, String operation, JSONObject observer) {
        AgentPushService.getInstance().pushToViewers(client, PushClient.generateViewId(dialog.getNode()), operation, observer);
    }

    private JSONObject serializeObserverInfo(Account account) {
        JSONObject observer = new JSONObject();
        observer.put("id", account.getId());
        observer.put("fullName", account.getUser().getInfo().getFullname());
        observer.put("email", account.getUser().getInfo().getEmail());
        observer.put("photo", account.getUser().getInfo().getPhoto());
        observer.put("entity", dialog.getEntityId());
        observer.put("field", dialog.getFieldPath());
        return observer;
    }
}
