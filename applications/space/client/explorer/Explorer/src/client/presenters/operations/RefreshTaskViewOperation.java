package client.presenters.operations;

import client.core.messages.LoadingTaskError;
import client.core.model.Task;
import client.core.model.TaskView;
import client.presenters.displays.Display;
import client.presenters.displays.entity.TaskDisplay;
import client.services.TaskService;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RefreshTaskViewOperation extends RefreshViewOperation<Task, TaskView> {

	public static final Type TYPE = new Type("RefreshTaskViewOperation", RefreshViewOperation.TYPE);

	public RefreshTaskViewOperation(Context context, Task task) {
		super(context, task);
	}

	@Override
	protected TaskView getViewToRefresh() {
		return getTaskDisplay().getCurrentView();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task to refresh " + getEntity().getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		if (!requireRefresh())
			return;

		TaskService service = services.getTaskService();
		service.open(entity.getId(), new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				refresh(task);
			}

			@Override
			public void failure(String error) {
				getMessageDisplay().hideLoading();
				executeFailed(new LoadingTaskError(error));
			}
		});
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.SHOW_TASK_VIEW);
	}

	private boolean requireRefresh() {
		TaskDisplay taskDisplay = getTaskDisplay();
		return taskDisplay != null && taskDisplay.getCurrentView() != null;
	}

	private TaskDisplay getTaskDisplay() {
		Display display = context.getCanvas();

		if (!(display instanceof TaskDisplay))
			return null;

		return (TaskDisplay)display;
	}

}
