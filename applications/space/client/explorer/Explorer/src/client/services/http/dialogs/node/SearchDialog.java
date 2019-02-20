package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class SearchDialog extends HttpDialog {

    private final Node container;

    public SearchDialog(Node container, int start, int limit) {
        this.container = container;
        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "node$search";
    }

    @Override
    public String getEntityId() {
        return container.getId();
    }
}
