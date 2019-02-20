package client.presenters.displays.view;

import client.core.model.*;
import client.core.model.Task.State;
import client.core.model.workmap.Action;
import client.presenters.displays.entity.workmap.TaskHistoryDisplay;
import client.presenters.displays.entity.workmap.TaskStateActionDisplay;
import cosmos.types.Date;

public class TaskStateViewDisplay extends TaskViewDisplay<TaskStateView> {
	private TaskStateActionDisplay actionDisplay;

    public static final Type TYPE = new Type("TaskStateViewDisplay", TaskViewDisplay.TYPE);

	public TaskStateViewDisplay(Task task, TaskStateView view) {
		super(task, view);
	}

	@Override
	protected void onInjectServices() {
		addAction();
		addHistory();
	}

	public void refresh() {
		notifyTitle();
		notifyDate();
		notifyState();

		refreshAction();
	}

	public String getTitle() {
		Action action = getEntity().getWorkMap().getPlace().getAction();

		if (action == null)
			return "";

		return action.getDefinition().getLabel();
	}

	public Date getDate() {
		return getEntity().getUpdateDate();
	}

	public State getState() {
		return getEntity().getState();
	}

	private void addAction() {
		Action action = getEntity().getAction();

		if (action == null)
			return;

		actionDisplay = new TaskStateActionDisplay.Builder<>().build(getEntity(), action);
		actionDisplay.inject(services);
		addChild(actionDisplay);
	}

	private void refreshAction() {
		Task task = getEntity();
		Action action = task.getAction();

		if (action == null)
			return;

		actionDisplay.setTarget(task, action);
		actionDisplay.refresh();
	}

	private void addHistory() {
        TaskHistoryDisplay historyDisplay = new TaskHistoryDisplay(getEntity());
		historyDisplay.inject(services);
		historyDisplay.firstPage();
		addChild(historyDisplay);
	}

	private void notifyTitle() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.title();
			}
		});
	}

	private void notifyDate() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.date();
			}
		});
	}

	private void notifyState() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.state();
			}
		});
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public static class Builder extends TaskViewDisplay.Builder<TaskStateView> {

		protected static void register() {
			registerBuilder(Activity.CLASS_NAME.toString() + TaskStateView.CLASS_NAME.toString(), new Builder());
			registerBuilder(Service.CLASS_NAME.toString() + TaskStateView.CLASS_NAME.toString(), new Builder());
			registerBuilder(Job.CLASS_NAME.toString() + TaskStateView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Task entity, TaskStateView view) {
			return new TaskStateViewDisplay(entity, view);
		}
	}

	public interface Hook extends TaskViewDisplay.Hook {
		void title();
		void date();
		void state();
	}

}
