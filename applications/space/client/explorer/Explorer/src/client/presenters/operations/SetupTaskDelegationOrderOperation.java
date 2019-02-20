package client.presenters.operations;

import client.core.messages.SettingTaskDelegationOrderError;
import client.core.model.Task;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupTaskDelegationOrderOperation extends TaskActionOperation {

	public static final Type TYPE = new Type("SetupTaskDelegationOrderOperation", TaskActionOperation.TYPE);

	public SetupTaskDelegationOrderOperation(Context context, Task task) {
		super(context, task);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task to abort: " + task.getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (disableButtonWhenExecute())
			disable();

		this.services.getTaskService().setupDelegationOrder(task, new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				refreshCanvas(task);
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new SettingTaskDelegationOrderError(details));
			}
		});
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SETUP_TASK_DELEGATION);
	}

}
