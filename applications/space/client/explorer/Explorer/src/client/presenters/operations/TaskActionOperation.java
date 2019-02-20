package client.presenters.operations;

import client.core.model.Task;
import client.presenters.displays.view.TaskStateViewDisplay;
import client.presenters.displays.view.ViewDisplay;
import cosmos.presenters.Presenter;

public abstract class TaskActionOperation extends Operation {
	protected final Task task;

	public static final Type TYPE = new Type("TaskActionOperation", Operation.TYPE);

	public TaskActionOperation(Context context, Task task) {
		super(context);
		this.task = task;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	protected void refreshCanvas(Task task) {
		TaskStateViewDisplay display = context.getCanvas();

		Presenter owner = display.getOwner();
		owner.removeChild(display);
		owner.addChild(new ViewDisplay.Builder<>().build(task, display.getView()));
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

}
