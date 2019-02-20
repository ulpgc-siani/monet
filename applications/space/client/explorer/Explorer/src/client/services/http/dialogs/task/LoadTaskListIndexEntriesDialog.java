package client.services.http.dialogs.task;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.Order;
import client.core.model.TaskList;
import client.services.http.dialogs.HttpDialog;
import client.services.http.serializers.FilterSerializer;
import client.services.http.serializers.OrderSerializer;

public class LoadTaskListIndexEntriesDialog extends HttpDialog {
    public LoadTaskListIndexEntriesDialog(TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit) {
        add("situation", situation.toString());
        add("inbox", inbox.toString());
        add("filters", new FilterSerializer().serializeList(filters));
        add("orders", new OrderSerializer().serializeList(orders));
        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "tasks$entries";
    }

    @Override
    public String getEntityId() {
        return null;
    }
}
