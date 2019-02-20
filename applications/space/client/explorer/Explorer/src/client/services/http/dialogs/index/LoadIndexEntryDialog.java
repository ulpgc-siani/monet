package client.services.http.dialogs.index;

import client.core.model.Index;
import client.core.model.Node;
import client.services.IndexService;

public class LoadIndexEntryDialog extends IndexDialog {
    private final Index index;

    public LoadIndexEntryDialog(Index index, IndexService.Scope scope, Node entryNode) {
        this.index = index;

        addScope(scope);
        add("node", entryNode.getId());
    }

    @Override
    public String getOperation() {
        return "index$entry";
    }

    @Override
    public String getEntityId() {
        return index.getId();
    }

}
