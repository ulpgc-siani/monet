package client.services.http.dialogs.node;

import client.core.model.Node;

public class DownloadNodeFileDialog extends DownloadNodeDialog {

    public DownloadNodeFileDialog(Node node, String fileId) {
        super(node);
        add("resource", fileId);
    }

    @Override
    public String getOperation() {
        return "node$download_file";
    }
}
