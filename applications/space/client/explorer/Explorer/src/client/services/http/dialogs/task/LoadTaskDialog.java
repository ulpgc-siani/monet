package client.services.http.dialogs.task;

import client.services.http.dialogs.HttpDialog;

public class LoadTaskDialog extends HttpDialog {
    private final String id;

    public LoadTaskDialog(String id) {
        this.id = id;
    }

    @Override
    public String getOperation() {
        return "task";
    }

    @Override
    public String getEntityId() {
        return id;
    }
}
