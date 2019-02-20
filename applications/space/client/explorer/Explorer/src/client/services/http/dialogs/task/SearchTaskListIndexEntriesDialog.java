package client.services.http.dialogs.task;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.Order;
import client.core.model.TaskList;

public class SearchTaskListIndexEntriesDialog extends LoadTaskListIndexEntriesDialog {

    public SearchTaskListIndexEntriesDialog(String condition, TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit) {
        super(situation, inbox, filters, orders, start, limit);
        add("condition", condition);
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
