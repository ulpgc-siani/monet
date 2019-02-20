package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class HelpPageDialog extends HttpDialog {

    private final Node node;

    public HelpPageDialog(Node node) {
        this.node = node;
    }

    @Override
    public String getOperation() {
        return "node$help";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
