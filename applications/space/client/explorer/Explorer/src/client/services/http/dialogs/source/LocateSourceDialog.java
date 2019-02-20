package client.services.http.dialogs.source;

import client.services.http.dialogs.HttpDialog;

public class LocateSourceDialog extends HttpDialog {
    private String key;

    public LocateSourceDialog(String key, String url) {
        this.key = key;
        add("url", url);
    }

    @Override
    public String getOperation() {
        return "source$locate";
    }

    @Override
    public String getEntityId() {
        return key;
    }
}
