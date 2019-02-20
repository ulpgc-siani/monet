package client.services.http.dialogs.node;

import client.services.http.dialogs.HttpDialog;

public class LoadNodeDialog extends HttpDialog {

    private final String id;

    public LoadNodeDialog(String id) {
        this.id = id;
    }

    @Override
    public String getOperation() {
        return "node";
    }

    @Override
    public String getEntityId() {
        return id;
    }
}
