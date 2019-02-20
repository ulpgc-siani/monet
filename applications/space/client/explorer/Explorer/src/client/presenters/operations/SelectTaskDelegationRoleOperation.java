package client.presenters.operations;

import client.core.messages.SelectingTaskDelegationRoleError;
import client.core.model.Role;
import client.core.model.Task;
import client.services.TaskService;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectTaskDelegationRoleOperation extends TaskActionOperation {
	private final Role role;

	public static final Type TYPE = new Type("SelectTaskDelegationRoleOperation", TaskActionOperation.TYPE);

	public SelectTaskDelegationRoleOperation(Context context, Task task, Role role) {
		super(context, task);
		this.role = role;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		final TaskService taskService = services.getTaskService();
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task to abort: " + task.getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (disableButtonWhenExecute())
			disable();

		taskService.selectDelegationRole(task, role, new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				refreshCanvas(task);
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new SelectingTaskDelegationRoleError(details));
			}
		});
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SELECT_TASK_DELEGATION_ROLE);
	}

}
