package client.services.http.dialogs.node;

import client.core.model.Command;
import client.core.model.Node;
import client.services.http.dialogs.HttpDialog;

public class CommandDialog extends HttpDialog {

    private final Node node;

    public CommandDialog(Node node, Command command) {
        this.node = node;
        add("name", command.getKey().getName());
    }

    @Override
    public String getOperation() {
        return "command$execute";
    }

    @Override
    public String getEntityId() {
        return node.getId();
    }
}
