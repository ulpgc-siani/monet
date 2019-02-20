package client.services.http.dialogs.node;

import client.core.model.Node;

public class DownloadNodeThumbnailDialog extends DownloadNodeImageDialog {

    public DownloadNodeThumbnailDialog(Node node, String imageId) {
        super(node, imageId);
        add("thumb", true);
    }
}
