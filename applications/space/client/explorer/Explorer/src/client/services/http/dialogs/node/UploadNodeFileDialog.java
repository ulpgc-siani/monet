package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class UploadNodeFileDialog extends HttpDialog {

    private final Node node;

    public UploadNodeFileDialog(Node node, String filename, String data) {
        this.node = node;
        add("resource", filename);
        add("data", data);
        add("rnd", Math.random());
    }

    @Override
    public String getOperation() {
        return "node$upload_file";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
