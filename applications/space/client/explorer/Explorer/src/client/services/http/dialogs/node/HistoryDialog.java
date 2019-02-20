package client.services.http.dialogs.node;

import client.services.http.dialogs.HttpDialog;

public class HistoryDialog extends HttpDialog {

    private final String store;

    public HistoryDialog(String store, String condition, int start, int limit) {
        this.store = store;

        if (condition != null)
            add("condition", condition);

        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "history";
    }

    @Override
    public String getEntityId() {
        return store;
    }

}
