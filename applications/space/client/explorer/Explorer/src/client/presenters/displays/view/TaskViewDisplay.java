package client.presenters.displays.view;

import client.core.model.Task;
import client.core.model.TaskView;

public abstract class TaskViewDisplay<ViewType extends TaskView> extends ViewDisplay<Task, ViewType> {

	public static final Type TYPE = new Type("TaskViewDisplay", ViewDisplay.TYPE);

	public TaskViewDisplay(Task task, ViewType view) {
        super(task, view);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public static class Builder<ViewType extends TaskView> extends ViewDisplay.Builder<Task, ViewType> {
	}

}
