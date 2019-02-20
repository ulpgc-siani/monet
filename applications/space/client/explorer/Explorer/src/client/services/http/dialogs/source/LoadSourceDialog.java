package client.services.http.dialogs.source;

import client.services.http.dialogs.HttpDialog;

public class LoadSourceDialog extends HttpDialog {
    private String id;

    public LoadSourceDialog(String id) {
        this.id = id;
    }

    @Override
    public String getOperation() {
        return "source";
    }

    @Override
    public String getEntityId() {
        return id;
    }
}
