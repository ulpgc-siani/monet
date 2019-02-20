package client.services.http.dialogs.index;

import client.core.model.Index;
import client.services.IndexService;

public class HistoryDialog extends IndexDialog {

    private final Index index;

    public HistoryDialog(Index index, IndexService.Scope scope, String store, String condition, int start, int limit) {
        this.index = index;
        addScope(scope);

        if (condition != null)
            add("condition", condition);

        add("start", start);
        add("limit", limit);
        add("store", store);
    }

    @Override
    public String getOperation() {
        return "node$entries_history";
    }

    @Override
    public String getEntityId() {
        return index.getId();
    }
}
