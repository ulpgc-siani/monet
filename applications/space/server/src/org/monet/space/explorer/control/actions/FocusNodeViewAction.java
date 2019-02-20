package org.monet.space.explorer.control.actions;

import net.minidev.json.JSONObject;
import org.monet.space.explorer.control.dialogs.NodeDialog;
import org.monet.space.explorer.control.displays.Display;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.PushClient;
import org.monet.space.kernel.model.UserPushContext;

import java.io.IOException;

import static org.monet.space.kernel.listeners.ListenerPushService.PushClientMessages;

public class FocusNodeViewAction extends Action<NodeDialog, Display<String>> {

    @Override
    public void execute() throws IOException {
        AgentPushService agentPushService = AgentPushService.getInstance();

        PushClient client = agentPushService.getClientView(getAccount().getId(), idSession, dialog.getInstanceId());
        agentPushService.pushToViewers(client, PushClient.generateViewId(dialog.getNode()), PushClientMessages.ADD_OBSERVER, serializeObserverInfo(getAccount()));
        agentPushService.updateClientView(getAccount().getId(), idSession, dialog.getInstanceId(), PushClient.generateViewId(dialog.getNode()));

        for (UserPushContext user: agentPushService.getViewObservers(PushClient.generateViewId(dialog.getNode())))
            if (!getAccount().getId().equals(user.getUserId()))
                agentPushService.push(getAccount().getId(), PushClientMessages.ADD_OBSERVER, serializeContext(user));

        display.write("OK");
    }

    private JSONObject serializeContext(UserPushContext user) {
        final JSONObject context = new JSONObject();
        context.put("id", user.getUserId());
        context.put("fullName", user.getUserFullname());
        context.put("email", user.getUserEmail());
        context.put("photo", user.getUserPhoto());
        context.put("entity", dialog.getEntityId());
        return context;
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
