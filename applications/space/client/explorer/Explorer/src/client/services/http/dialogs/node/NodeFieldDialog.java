package client.services.http.dialogs.node;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.Space;

abstract class NodeFieldDialog extends NodeViewDialog {

    public NodeFieldDialog(Node node, Field field, Space space) {
        super(node, space);
        add("field", field.getPath());
    }
}
