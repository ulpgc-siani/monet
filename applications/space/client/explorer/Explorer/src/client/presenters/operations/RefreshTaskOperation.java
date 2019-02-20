package client.presenters.operations;

import client.core.messages.AbortingTaskError;
import client.core.model.Task;
import client.core.model.TaskView;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.EntityVisitingDisplay;
import client.presenters.displays.VisitingDisplay;
import client.presenters.displays.entity.TaskDisplay;
import client.services.TranslatorService;
import client.services.callback.TaskCallback;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RefreshTaskOperation extends ShowOperation<Task, TaskView> {

	public static final Type TYPE = new Type("RefreshTaskOperation", Operation.TYPE);

	public RefreshTaskOperation(Context context, Task task, TaskView taskView) {
		super(context, task, taskView);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, getType().toString() + " called. Task to refresh: " + getEntity().getId());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		services.getTaskService().open(getEntity().getId(), new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				refreshCanvas(task);
				executePerformed();
			}

			@Override
			public void failure(String details) {
				getMessageDisplay().hideLoading();
				executeFailed(new AbortingTaskError(details));
			}
		});
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.REFRESH_TASK);
	}

	protected void refreshCanvas(Task task) {
		TaskView view = getView();

		if (view == null)
			view = (TaskView) task.getViews().getDefaultView();

		EntityDisplay entityDisplay = new TaskDisplay.Builder().build(task, view);
		EntityVisitingDisplay visitingDisplay = new EntityVisitingDisplay(entityDisplay, context.getReferral());
		Type type = VisitingDisplay.TYPE;
		Presenter display = context.getCanvas();

		if (display.existsChild(type))
			display.removeChild(type);

		display.addChild(visitingDisplay);

		this.executePerformed();
	}

	@Override
	public String getDescription() {
		return null;
	}
}
