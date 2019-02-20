package client.services.http.dialogs.task;

import client.core.model.Role;
import client.core.model.Task;

public class SelectTaskDelegationRoleDialog extends TaskDialog {
    public SelectTaskDelegationRoleDialog(Task task, Role role) {
        super(task);

        add("role", role.getId());
    }

    @Override
    public String getOperation() {
        return "task$delegation_select";
    }
}
