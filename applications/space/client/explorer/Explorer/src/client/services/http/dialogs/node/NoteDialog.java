package client.services.http.dialogs.node;

import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class NoteDialog extends HttpDialog {

    private final Node node;

    public NoteDialog(Node node, String name, String value) {
        this.node = node;

        add("name", name);
        add("value", value);
    }

    @Override
    public String getOperation() {
        return "note$save";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }

}
