package client.services.http.dialogs.task;

import client.core.model.Task;
import client.services.http.dialogs.HttpDialog;

public class LoadTaskHistoryDialog extends HttpDialog {
    private Task task;

    public LoadTaskHistoryDialog(Task task, int start, int limit) {
        this.task = task;

        add("start", start);
        add("limit", limit);
    }

    @Override
    public String getOperation() {
        return "task$history";
    }

    @Override
    public String getEntityId() {
        return task.getId();
    }
}
