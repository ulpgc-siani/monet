package client.presenters.operations;

import client.core.messages.LoadingTaskError;
import client.core.model.Task;
import client.core.model.TaskView;
import client.presenters.displays.*;
import client.presenters.displays.entity.TaskDisplay;
import client.services.TaskService;
import client.services.callback.TaskCallback;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowTaskOperation extends ShowOperation<Task, TaskView> {

	public static final Type TYPE = new Type("ShowTaskOperation", Operation.TYPE);

	public ShowTaskOperation(Context context, Task task, TaskView view) {
		super(context, task, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Task: " + entity.getLabel());

		getMessageDisplay().showLoading(services.getTranslatorService().getLoadingLabel());
		TaskService service = this.services.getTaskService();
		service.open(entity.getId(), new TaskCallback() {
			@Override
			public void success(Task task) {
				getMessageDisplay().hideLoading();
				ShowTaskOperation.this.refresh(task);
			}

			@Override
			public void failure(String error) {
				getMessageDisplay().hideLoading();
				ShowTaskOperation.this.executeFailed(new LoadingTaskError(error));
			}
		});
	}

	protected void refresh(Task task) {

		if (view == null)
			view = (TaskView)task.getViews().getDefaultView();

		this.refreshExplorationDisplay();
		this.refreshCanvas(task);
	}

	private void refreshExplorationDisplay() {
		Presenter display = this.context.getCanvas();

		if (!(display instanceof ApplicationDisplay))
			return;

		ExplorationDisplay explorationDisplay = ((ApplicationDisplay) display).getExplorationDisplay();

		explorationDisplay.clearChildren();
		explorationDisplay.addDeeply(new Operation[] { this }, explorationDisplay.getRoot());
		explorationDisplay.activate(this);
	}

	private void refreshCanvas(Task task) {
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
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		if (view != null)
			return view.getLabel();
		return entity.getLabel();
	}

	@Override
	public String getDescription() {
		return entity.getDescription();
	}
}
