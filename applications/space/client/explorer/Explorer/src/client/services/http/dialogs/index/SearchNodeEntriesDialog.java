package client.services.http.dialogs.index;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class SearchNodeEntriesDialog extends HttpDialog {
    private final Node node;

    public SearchNodeEntriesDialog(Node node, String condition, int start, int limit) {
        this.node = node;
        add("condition", condition);
        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "node$entries_history";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
