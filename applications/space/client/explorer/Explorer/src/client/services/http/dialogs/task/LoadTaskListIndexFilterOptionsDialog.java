package client.services.http.dialogs.task;

import client.core.model.Filter;
import client.services.http.dialogs.HttpDialog;
import client.services.http.serializers.FilterSerializer;

public class LoadTaskListIndexFilterOptionsDialog extends HttpDialog {
    public LoadTaskListIndexFilterOptionsDialog(Filter filter) {
        add("filter", new FilterSerializer().serialize(filter));
    }

    @Override
    public String getOperation() {
        return "tasks$options";
    }

    @Override
    public String getEntityId() {
        return null;
    }
}
