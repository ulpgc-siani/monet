package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class AddNodeDialog extends HttpDialog {

    private final Node parent;

    public AddNodeDialog(String code, Node parent) {
        this.parent = parent;

        add("code", code);
    }

    @Override
    public String getOperation() {
        return "node$add";
    }

    @Override
    public String getEntityId() {
        return parent.getId();
    }
}
