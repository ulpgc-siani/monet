package client.services.http.dialogs.node;

import client.services.http.dialogs.HttpDialog;

public class PreviewNodeDialog extends HttpDialog {

    private final String nodeId;

    public PreviewNodeDialog(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getOperation() {
        return "node$preview";
    }

    @Override
    public String getEntityId() {
        return nodeId;
    }
}
