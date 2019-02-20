package client.services.http.dialogs.task;

import client.core.model.Task;
import client.core.model.User;

public class SaveTaskOwnerDialog extends TaskDialog {

    public SaveTaskOwnerDialog(Task task, User owner, String reason) {
        super(task);

        add("owner", owner.getId());
        add("reason", reason);
    }

    @Override
    public String getOperation() {
        return "task$owner";
    }
}
