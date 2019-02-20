package client.services.http.dialogs.task;

import client.core.model.Task;

public class SolveTaskEditionDialog extends TaskDialog {

    public SolveTaskEditionDialog(Task task) {
        super(task);
    }

    @Override
    public String getOperation() {
        return "task$edition_solve";
    }
}
