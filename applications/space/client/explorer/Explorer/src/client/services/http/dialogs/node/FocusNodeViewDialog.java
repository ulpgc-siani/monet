package client.services.http.dialogs.node;

import client.core.model.Node;
import client.core.model.Space;

public class FocusNodeViewDialog extends NodeViewDialog {

    public FocusNodeViewDialog(Node node, Space space) {
        super(node, space);
    }

    @Override
    public String getOperation() {
        return "focusnodeview";
    }
}
