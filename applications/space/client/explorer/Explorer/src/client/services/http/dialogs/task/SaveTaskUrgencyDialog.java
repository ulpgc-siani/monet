package client.services.http.dialogs.task;

import client.core.model.Task;

public class SaveTaskUrgencyDialog extends TaskDialog {

    public SaveTaskUrgencyDialog(Task task, boolean urgent) {
        super(task);

        add("urgent", urgent);
    }

    @Override
    public String getOperation() {
        return "task$urgency";
    }
}
