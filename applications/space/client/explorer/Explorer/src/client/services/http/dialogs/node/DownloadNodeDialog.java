package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class DownloadNodeDialog extends HttpDialog {

    private final Node node;

    public DownloadNodeDialog(Node node) {
        this.node = node;

        add("rnd", Math.random());
    }

    @Override
    public String getOperation() {
        return "node$download";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
