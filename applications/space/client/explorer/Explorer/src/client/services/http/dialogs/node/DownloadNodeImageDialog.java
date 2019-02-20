package client.services.http.dialogs.node;

import client.core.model.Node;

public class DownloadNodeImageDialog extends DownloadNodeFileDialog {

    public DownloadNodeImageDialog(Node node, String imageId) {
        super(node, imageId);
    }

    @Override
    public String getOperation() {
        return "node$download_image";
    }
}
