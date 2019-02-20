package client.services.http.dialogs.node;

import client.core.model.Node;
import client.core.model.Space;
import client.services.http.dialogs.HttpDialog;

abstract class NodeViewDialog extends HttpDialog {

    private final Node node;

    public NodeViewDialog(Node node, Space space) {
        this.node = node;
        add("instance_id", space.getInstanceId());
    }

    @Override
    public abstract String getOperation();

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
