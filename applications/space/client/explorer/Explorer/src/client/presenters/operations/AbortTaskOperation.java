package client.presenters.operations;

import client.core.messages.AbortingTaskError;
import client.core.model.Task;
import client.core.model.TaskView;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.EntityVisitingDisplay;
import client.presenters.displays.VisitingDisplay;
import client.presenters.displays.entity.TaskDisplay;
import client.services.TranslatorService;
import client.services.callback.VoidCallback;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AbortTaskOperation extends ShowOperation<Task, TaskView> {

	public static final Type TYPE = new Type("AbortTaskOperation", Operation.TYPE);

	public AbortTaskOperation(Context context, Task task, TaskView view) {
		super(context, task, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task to abort: " + getEntity().getLabel());
		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());

		this.services.getTaskService().abort(getEntity(), new VoidCallback() {
			@Override
			public void success(Void value) {
				getMessageDisplay().hideLoading();
				refreshCanvas(getEntity());
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
		return services.getTranslatorService().translate(TranslatorService.OperationLabel.ABORT_TASK);
	}

	protected void refreshCanvas(Task task) {
		EntityDisplay entityDisplay = new TaskDisplay.Builder().build(task, getView());
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
