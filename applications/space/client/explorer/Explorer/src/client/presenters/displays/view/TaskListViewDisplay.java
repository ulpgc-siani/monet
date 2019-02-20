package client.presenters.displays.view;

import client.core.model.*;
import client.presenters.displays.TaskListIndexDisplay;
import client.presenters.operations.Operation;
import client.presenters.operations.SetTasksOwnerOperation;
import client.presenters.operations.ShowTaskOperation;

public class TaskListViewDisplay<ViewType extends TaskListView> extends ViewDisplay<TaskList, ViewType> {

	public static final Type TYPE = new Type("TaskListViewDisplay", ViewDisplay.TYPE);

	public TaskListViewDisplay(TaskList taskList, ViewType taskView) {
        super(taskList, taskView);
	}

	@Override
	protected void onInjectServices() {
		addIndex();
	}

	private void addIndex() {
		TaskListIndexDisplay indexDisplay = new TaskListIndexDisplay(getEntity(), new TaskListIndexDisplay.Handler() {
			@Override
			public void onActivate(TaskListIndexDisplay display, TaskListIndexEntry entry) {
				showTask(getOperationContext(), entry.getEntity());
			}

			@Override
			public void onSelect(TaskListIndexDisplay display, TaskListIndexEntry entry) {
				refreshOperations(display);
			}

			@Override
			public void onDelete(final TaskListIndexDisplay display, TaskListIndexEntry entry) {
			}

			@Override
			public void onUnSelect(TaskListIndexDisplay display, TaskListIndexEntry entry) {
				refreshOperations(display);
			}

			@Override
			public void onUnSelectAll(TaskListIndexDisplay display) {
				refreshOperations(display);
			}

			private void refreshOperations(TaskListIndexDisplay indexDisplay) {
				boolean enabled = indexDisplay.getSelectionCount()>0;
				List<cosmos.presenters.Operation> operations = indexDisplay.getOperations();
				for (cosmos.presenters.Operation operation : operations) {
					if (operation instanceof SetTasksOwnerOperation)
						operation.setEnabled(enabled);
				}
			}

		}, getState());

		addOperations(indexDisplay);
		addChild(indexDisplay);
	}

	private TaskList.Situation getState() {
		return getView().getSituation();
	}

	private void addOperations(TaskListIndexDisplay taskListIndexDisplay) {
//		taskListIndexDisplay.addChild(createSetTasksOwnerOperation(taskListIndexDisplay));
//		taskListIndexDisplay.addChild(createUnSetTasksOwnerOperation(taskListIndexDisplay));
	}

	private void showTask(final cosmos.presenters.Operation.Context context, Task entity) {
		ViewList<TaskView> views = entity.getViews();

		Operation operation = new ShowTaskOperation(context, entity, views.getDefaultView());
		operation.inject(services);
		operation.execute();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public static class Builder extends ViewDisplay.Builder<TaskList, TaskListView> {

		protected static void register() {
			registerBuilder(TaskList.CLASS_NAME.toString() + TaskListView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(TaskList entity, TaskListView view) {
			return new TaskListViewDisplay(entity, view);
		}
	}

}
