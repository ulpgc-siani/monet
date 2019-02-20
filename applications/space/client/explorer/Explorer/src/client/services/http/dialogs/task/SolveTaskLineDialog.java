package client.services.http.dialogs.task;

import client.core.model.Task;
import client.core.model.workmap.LineAction;

public class SolveTaskLineDialog extends TaskDialog {

    public SolveTaskLineDialog(Task task, LineAction.Stop stop) {
        super(task);

        add("stop", stop.getCode());
    }

    @Override
    public String getOperation() {
        return "task$line_solve";
    }
}
