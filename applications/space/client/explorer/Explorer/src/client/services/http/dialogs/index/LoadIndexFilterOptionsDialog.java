package client.services.http.dialogs.index;

import client.core.model.Filter;
import client.core.model.Index;
import client.services.IndexService;
import client.services.http.serializers.FilterSerializer;

public class LoadIndexFilterOptionsDialog extends IndexDialog {
    private final Index index;

    public LoadIndexFilterOptionsDialog(Index index, IndexService.Scope scope, Filter filter) {
        this.index = index;

        addScope(scope);
        add("filter", new FilterSerializer().serialize(filter));
    }

    @Override
    public String getOperation() {
        return "index$options";
    }

    @Override
    public String getEntityId() {
        return index.getId();
    }
}
