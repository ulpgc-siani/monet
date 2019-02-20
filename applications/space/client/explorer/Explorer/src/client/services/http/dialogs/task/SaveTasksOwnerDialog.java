package client.services.http.dialogs.task;

import client.core.model.Task;
import client.core.model.User;

public class SaveTasksOwnerDialog extends TasksDialog {

    public SaveTasksOwnerDialog(Task[] tasks, User owner, String reason) {
        super(tasks);

        add("owner", owner.getId());
        add("reason", reason);
    }

    @Override
    public String getOperation() {
        return "tasks$owner";
    }
}
