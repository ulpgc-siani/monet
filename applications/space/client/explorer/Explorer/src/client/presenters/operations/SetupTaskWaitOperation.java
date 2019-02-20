package client.presenters.operations;

import client.core.messages.SettingTaskWaitError;
import client.core.model.Task;
import client.core.model.workmap.WaitAction.Scale;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupTaskWaitOperation extends TaskActionOperation {
	private final Scale scale;
	private final int value;

	public static final Type TYPE = new Type("SetupTaskWaitOperation", TaskActionOperation.TYPE);

	public SetupTaskWaitOperation(Context context, Task task, Scale scale, int value) {
		super(context, task);
		this.scale = scale;
		this.value = value;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task wait to solve: " + task.getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (disableButtonWhenExecute())
			disable();

		this.services.getTaskService().setupWait(task, scale, value, new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				refreshCanvas(task);
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new SettingTaskWaitError(details));
			}
		});
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SOLVE_TASK_LINE);
	}

}
