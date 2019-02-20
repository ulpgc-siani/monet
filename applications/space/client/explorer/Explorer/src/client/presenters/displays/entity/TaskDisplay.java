package client.presenters.displays.entity;

import client.core.model.Task;
import client.core.model.TaskView;
import client.core.model.ViewList;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.OperationListDisplay;
import client.presenters.displays.view.LazyLoadViewListDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.AbortTaskOperation;
import client.presenters.operations.RefreshTaskOperation;
import client.presenters.operations.ShowTaskOperation;
import client.services.NotificationService;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

public class TaskDisplay<T extends Task> extends EntityDisplay<T, TaskView> {
	public static final Type TYPE = new Type("TaskDisplay", EntityDisplay.TYPE);
	private NotificationService.UpdateTaskStateListener listener;

	protected TaskDisplay(T task, TaskView view) {
		super(task, view);
	}

	@Override
	protected void onInjectServices() {
		listener = new NotificationService.UpdateTaskStateListener() {
			@Override
			public void notify(String id) {
				if (getEntity().getId().equals(id))
					refreshTask(services.getSpaceService().getEntityFactory().createTask(id));
			}
		};
		services.getNotificationService().registerListener(listener);
		addViews();
		addOperations();
		addCurrentView();
	}

	@Override
	public void presenterRemoved(Presenter presenter) {
		services.getNotificationService().deregisterListener(listener);
		super.presenterRemoved(presenter);
	}

	@Override
	public String getLabel() {
		return getEntity().getLabel();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public int getViewsCount() {
		return getEntity().getViews().size();
	}

	public Task.Type getEntityType() {
		Task task = this.getEntity();
		return task.getType();
	}

	public TaskView getCurrentView() {
		ViewList<TaskView> views = getEntity().getViews();

		if (getView() != null)
			return getView();

		TaskView defaultView = views.getDefaultView();
		if (defaultView != null)
			return defaultView;

		return null;
	}

	private void addViews() {
		Task task = getEntity();
		ViewList<TaskView> views = task.getViews();
		Map<TaskView, ShowTaskOperation> operations = new HashMap<>();

		for (TaskView taskView : views)
			operations.put(taskView, createShowTaskOperation(task, taskView));

		LazyLoadViewListDisplay viewListDisplay = new LazyLoadViewListDisplay(task, views, operations);
		viewListDisplay.inject(services);
		viewListDisplay.setActiveView(getCurrentView());

		this.addChild(viewListDisplay);
	}

	private ShowTaskOperation createShowTaskOperation(Task task, TaskView taskView) {
		ShowTaskOperation operation = new ShowTaskOperation(getOperationContext(), task, taskView);
		operation.inject(services);
		return operation;
	}

	private void addCurrentView() {
		Task task = getEntity();
		TaskView currentView = getCurrentView();

		if (currentView == null)
			return;

		ViewDisplay display = new ViewDisplay.Builder().build(task, currentView);
		addChild(display);
	}

	protected void addOperations() {
		OperationListDisplay operationListDisplay = new OperationListDisplay();
		operationListDisplay.addChild(createAbortTaskOperation());
		operationListDisplay.addChild(createRefreshTaskOperation(getEntity(), getCurrentView()));
		addChild(operationListDisplay);
	}

	private AbortTaskOperation createAbortTaskOperation() {
		AbortTaskOperation operation = new AbortTaskOperation(getOperationContext(), getEntity(), getCurrentView());
		operation.inject(services);
		return operation;
	}

	private RefreshTaskOperation createRefreshTaskOperation(T task, TaskView currentView) {
		RefreshTaskOperation operation = new RefreshTaskOperation(getOperationContext(), task, currentView);
		operation.inject(services);
		return operation;
	}

    private void refreshTask(Task task) {
        createRefreshTaskOperation((T) task, null).execute();
    }

	public static class Builder {
		public TaskDisplay build(Task task, TaskView view) {
			return new TaskDisplay(task, view);
		}
	}

	public interface Hook extends EntityDisplay.Hook {
	}
}
