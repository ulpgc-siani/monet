package client.services.http.dialogs.node;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.Space;

public class FocusNodeFieldDialog extends NodeFieldDialog {

    public FocusNodeFieldDialog(Node node, Field field, Space space) {
        super(node, field, space);
    }

    @Override
    public String getOperation() {
        return "focusnodefield";
    }
}
