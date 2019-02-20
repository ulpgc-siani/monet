package client.services.http.dialogs.node;

import client.core.model.MultipleField;
import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class ChangeFieldOrderDialog extends HttpDialog {

    private Node node;

    public ChangeFieldOrderDialog(Node node, MultipleField parent, int previousPos, int newPos) {
        this.node = node;

        add("parent", parent.getPath());
        add("oldPos", previousPos);
        add("pos", newPos);
    }

    @Override
    public String getOperation() {
        return "field$order";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }

}
