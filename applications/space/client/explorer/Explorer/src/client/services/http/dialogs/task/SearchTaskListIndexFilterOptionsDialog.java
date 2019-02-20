package client.services.http.dialogs.task;

import client.core.model.Filter;

public class SearchTaskListIndexFilterOptionsDialog extends LoadTaskListIndexFilterOptionsDialog {

    public SearchTaskListIndexFilterOptionsDialog(Filter filter, String condition) {
        super(filter);
        add("condition", condition);
    }
}
