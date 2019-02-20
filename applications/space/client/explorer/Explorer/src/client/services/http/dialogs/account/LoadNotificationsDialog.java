package client.services.http.dialogs.account;

import client.services.http.dialogs.HttpDialog;

public class LoadNotificationsDialog extends HttpDialog {

    public LoadNotificationsDialog(int start, int limit) {
        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "notifications";
    }

    @Override
    public String getEntityId() {
        return null;
    }
}
