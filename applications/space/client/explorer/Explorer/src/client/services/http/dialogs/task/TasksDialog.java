package client.services.http.dialogs.task;

import client.core.model.Task;
import client.services.http.dialogs.HttpDialog;

abstract class TasksDialog extends HttpDialog {
    private Task[] tasks;

    public TasksDialog(Task[] tasks) {
        this.tasks = tasks;
    }
    @Override
    public String getEntityId() {
        return serializeIds(tasks);
    }
}
