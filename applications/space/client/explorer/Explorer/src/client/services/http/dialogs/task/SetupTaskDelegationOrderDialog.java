package client.services.http.dialogs.task;

import client.core.model.Task;

public class SetupTaskDelegationOrderDialog extends TaskDialog {
    public SetupTaskDelegationOrderDialog(Task task) {
        super(task);
    }

    @Override
    public String getOperation() {
        return "task$delegation_setup";
    }
}
