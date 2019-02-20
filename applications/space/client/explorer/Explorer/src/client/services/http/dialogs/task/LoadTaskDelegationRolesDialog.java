package client.services.http.dialogs.task;

import client.core.model.Task;

public class LoadTaskDelegationRolesDialog extends TaskDialog {
    public LoadTaskDelegationRolesDialog(Task task) {
        super(task);
    }

    @Override
    public String getOperation() {
        return "task$delegation_roles";
    }
}
