package client.presenters.displays.entity;

import client.core.model.TaskList;
import client.core.model.TaskListView;
import client.core.model.ViewList;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.OperationListDisplay;
import client.presenters.displays.view.LazyLoadViewListDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.ShowTaskListOperation;
import client.services.TranslatorService;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static cosmos.presenters.Operation.Context;

public class TaskListDisplay extends EntityDisplay<TaskList, TaskListView> {

	public static final Type TYPE = new Type("TaskListDisplay", EntityDisplay.TYPE);

	protected TaskListDisplay(TaskList taskList, TaskListView view) {
        super(taskList, view);
	}

	@Override
	protected void onInjectServices() {
		addViews();
		addCurrentView();
		addOperations();
	}

	@Override
	public String getLabel() {
		return services.getTranslatorService().translate(TranslatorService.Label.TASK_LIST);
	}

	private void addViews() {
		TaskList taskList = getEntity();
		ViewList<TaskListView> views = taskList.getViews();
		Map<TaskListView, ShowTaskListOperation> operations = new HashMap<>();

		for (TaskListView taskListView : views) {
			ShowTaskListOperation operation = new ShowTaskListOperation(new Context() {
				@Override
				public Presenter getCanvas() {
					return TaskListDisplay.this.getRootDisplay();
				}

				@Override
				public cosmos.presenters.Operation getReferral() {
					return getVisitingDisplayOperation();
				}
			}, taskList, taskListView);
			operation.inject(services);
			operations.put(taskListView, operation);
		}

		LazyLoadViewListDisplay viewListDisplay = new LazyLoadViewListDisplay(taskList, views, operations);
		viewListDisplay.setActiveView(getCurrentView());

		this.addChild(viewListDisplay);
	}

	private void addCurrentView() {
		TaskList taskList = getEntity();
		TaskListView currentView = getCurrentView();

		if (currentView == null)
			return;

		ViewDisplay display = new ViewDisplay.Builder().build(taskList, currentView);
		addChild(display);
	}

	public TaskListView getCurrentView() {
		ViewList<TaskListView> views = getEntity().getViews();

		if (getView() != null)
			return getView();

		TaskListView defaultView = views.getDefaultView();
		if (defaultView != null)
			return defaultView;

		return null;
	}

	private void addOperations() {
		OperationListDisplay operationListDisplay = new OperationListDisplay();
		this.addChild(operationListDisplay);
	}

	private Presenter getCanvas() {
		return this;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public static class Builder {
		public TaskListDisplay build(TaskList taskList, TaskListView view) {
            return new TaskListDisplay(taskList, view);
		}
	}

	public interface Hook extends EntityDisplay.Hook {
	}

}
