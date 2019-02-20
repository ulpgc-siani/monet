package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class DeleteNodesDialog extends HttpDialog {

    private Node[] nodes;

    public DeleteNodesDialog(Node[] nodes) {
        this.nodes = nodes;
    }

    @Override
    public String getOperation() {
        return "nodes$delete";
    }

    @Override
    public String getEntityId() {
        return serializeIds(nodes);
    }

}
