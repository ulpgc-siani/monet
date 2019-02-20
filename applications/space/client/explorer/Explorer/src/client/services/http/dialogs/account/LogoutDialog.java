package client.services.http.dialogs.account;

import client.services.http.dialogs.HttpDialog;

public class LogoutDialog extends HttpDialog {
    private String instanceId;

    public LogoutDialog(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String getOperation() {
        return "logout";
    }

    @Override
    public String getEntityId() {
        return instanceId;
    }
}
