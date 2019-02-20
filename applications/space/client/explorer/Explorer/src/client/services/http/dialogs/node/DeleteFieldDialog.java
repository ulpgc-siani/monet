package client.services.http.dialogs.node;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.Space;
import client.services.http.dialogs.HttpDialog;

public class DeleteFieldDialog extends HttpDialog {

    private final Node node;

    public DeleteFieldDialog(Node node, Field parent, int pos, Space space) {
        this.node = node;

        add("instance_id", space.getInstanceId());
        add("parent", parent.getPath());
        add("pos", pos);
    }

    @Override
    public String getOperation() {
        return "field$delete";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }

}
