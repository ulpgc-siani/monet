package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class DeleteNodeDialog extends HttpDialog {

    private final Node node;

    public DeleteNodeDialog(Node node) {
        this.node = node;
    }

    @Override
    public String getOperation() {
        return "node$delete";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
