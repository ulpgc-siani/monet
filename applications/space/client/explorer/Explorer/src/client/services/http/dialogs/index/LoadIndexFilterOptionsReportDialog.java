package client.services.http.dialogs.index;

import client.core.model.Filter;
import client.core.model.Index;
import client.services.IndexService;
import client.services.http.serializers.FilterSerializer;

public class LoadIndexFilterOptionsReportDialog extends IndexDialog {
    private final Index index;

    public LoadIndexFilterOptionsReportDialog(Index index, IndexService.Scope scope, Filter filter) {
        this.index = index;

        addScope(scope);
        add("filter", new FilterSerializer().serialize(filter));
    }

    @Override
    public String getOperation() {
        return "index$options_report";
    }

    @Override
    public String getEntityId() {
        return index.getId();
    }

}
