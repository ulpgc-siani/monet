package client.services.http.dialogs.node;

import client.core.model.Node;

public class UploadNodeImageDialog extends UploadNodeFileDialog {

    public UploadNodeImageDialog(Node node, String filename, String data) {
        super(node, filename, data);
    }

    @Override
    public String getOperation() {
        return "node$upload_image";
    }
}
