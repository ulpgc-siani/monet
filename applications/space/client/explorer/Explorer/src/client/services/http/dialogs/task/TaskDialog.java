package client.services.http.dialogs.task;

import client.core.model.Task;
import client.services.http.dialogs.HttpDialog;

abstract class TaskDialog extends HttpDialog {
    private Task task;

    public TaskDialog(Task task) {
        this.task = task;
    }

    @Override
    public String getEntityId() {
        return task.getId();
    }
}
