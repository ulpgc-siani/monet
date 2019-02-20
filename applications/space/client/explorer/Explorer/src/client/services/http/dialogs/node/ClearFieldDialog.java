package client.services.http.dialogs.node;

import client.core.model.MultipleField;
import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class ClearFieldDialog extends HttpDialog {

    private final Node node;

    public ClearFieldDialog(Node node, MultipleField field) {
        this.node = node;

        add("path", field.getPath());
    }

    @Override
    public String getOperation() {
        return "field$clear";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }

}
