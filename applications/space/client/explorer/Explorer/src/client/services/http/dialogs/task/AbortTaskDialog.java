package client.services.http.dialogs.task;

import client.core.model.Task;

public class AbortTaskDialog extends TaskDialog {

    public AbortTaskDialog(Task task) {
        super(task);
    }

    @Override
    public String getOperation() {
        return "task$abort";
    }
}
