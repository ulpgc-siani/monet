package client.presenters.operations;

import client.ExplorerApplication;
import client.core.model.Space;
import client.core.model.Task;
import client.core.model.factory.EntityFactory;
import client.services.Services;
import cosmos.presenters.*;
import cosmos.presenters.Operation;

public class ActionDispatcher {
	private Services services;

    public ActionDispatcher(Services services) {
		this.services = services;
	}

    public void dispatch(Space.Action action, Operation.Context context) {
        if (action instanceof Space.ShowHomeAction)
            dispatch((Space.ShowHomeAction)action, context);
        else if (action instanceof Space.ShowTaskAction)
            dispatch((Space.ShowTaskAction)action, context);
        else if (action instanceof Space.ShowNodeAction)
            dispatch((Space.ShowNodeAction)action, context);
    }

    private void dispatch(Space.ShowHomeAction action, Operation.Context context) {
        ShowHomeOperation operation = new ShowHomeOperation(context);
        operation.inject(services);
        operation.execute();
    }

    private void dispatch(Space.ShowTaskAction action, Operation.Context context) {
        Task task = getEntityFactory().createTask(action.getTaskId());

        ShowTaskOperation operation = new ShowTaskOperation(context, task, null);
        operation.inject(services);
        operation.execute();
    }

	private void dispatch(Space.ShowNodeAction action, Operation.Context context) {
		ShowNodeOperation operation = new ShowNodeOperation(context, getEntityFactory().createNode(action.getNodeId()), null);
		operation.inject(services);
		operation.execute();
	}

	private EntityFactory getEntityFactory() {
		return services.getSpaceService().getEntityFactory();
	}

}
