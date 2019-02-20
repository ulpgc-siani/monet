package client.presenters.displays.entity.workmap;

import client.core.model.Task;
import client.core.model.workmap.Action;
import client.presenters.displays.Display;

public abstract class TaskStateActionDisplay<A extends Action> extends Display {
	private Task task;
	private A action;

	public static final Type TYPE = new Type("TaskStateActionDisplay", Display.TYPE);

	public TaskStateActionDisplay(Task task, A action) {
        this.task = task;
		this.action = action;
	}

	@Override
	protected void onInjectServices() {
	}

	public Task getTask() {
		return task;
	}

	public A getAction() {
		return action;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public void setTarget(Task task, A action) {
		this.task = task;
		this.action = action;
	}

	public void refresh() {
		load();
	}

	public abstract void load();

	public static class Builder<A extends Action> extends client.presenters.displays.Display.Builder {

		protected static void register() {
			TaskStateDelegationActionDisplay.Builder.register();
//			TaskStateSendJobActionDisplay.Builder.register();
			TaskStateLineActionDisplay.Builder.register();
			TaskStateEditionActionDisplay.Builder.register();
//			TaskStateEnrollActionDisplay.Builder.register();
			TaskStateWaitActionDisplay.Builder.register();
		}

        public TaskStateActionDisplay build(Task task, A action) {
	        register();

			Builder builder = (Builder) getBuilder(action.getClassName());
	        if (builder == null)
		        return null;

	        return builder.build(task, action);
        }

	}

}
