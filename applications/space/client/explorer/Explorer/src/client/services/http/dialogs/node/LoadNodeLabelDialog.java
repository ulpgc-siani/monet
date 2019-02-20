package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class LoadNodeLabelDialog extends HttpDialog {

    private final String id;

    public LoadNodeLabelDialog(Node node) {
        this.id = node.getId();
    }

    @Override
    public String getOperation() {
        return "node$label";
    }

    @Override
    public String getEntityId() {
        return id;
    }
}
