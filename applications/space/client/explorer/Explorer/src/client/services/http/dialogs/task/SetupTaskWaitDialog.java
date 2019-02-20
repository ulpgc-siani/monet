package client.services.http.dialogs.task;

import client.core.model.Task;
import client.core.model.workmap.WaitAction;

public class SetupTaskWaitDialog extends TaskDialog {

    public SetupTaskWaitDialog(Task task, WaitAction.Scale scale, int value) {
        super(task);

        add("scale", scale.toString());
        add("value", value);
    }

    @Override
    public String getOperation() {
        return "task$wait_setup";
    }
}
