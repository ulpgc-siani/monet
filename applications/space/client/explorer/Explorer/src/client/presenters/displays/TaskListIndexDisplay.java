package client.presenters.displays;

import client.core.model.*;
import client.services.TaskService;
import client.services.callback.FilterOptionsCallback;
import client.services.callback.TaskListIndexEntriesCallback;
import client.services.callback.VoidCallback;

import java.util.HashMap;
import java.util.Map;

public class TaskListIndexDisplay extends IndexDisplay<TaskList, TaskListIndexEntry> {
	private final TaskList.Situation situation;
	private Map<TaskList.Situation, Integer> entriesCount = new HashMap<>();

	public static final Type TYPE = new Type("TaskListIndexDisplay", IndexDisplay.TYPE);

	public TaskListIndexDisplay(TaskList taskList, Handler handler, TaskList.Situation situation, int pageSize) {
		super(taskList, handler, pageSize);
		this.situation = situation;
	}

	public TaskListIndexDisplay(TaskList taskList, Handler handler, TaskList.Situation situation) {
		this(taskList, handler, situation, IndexDisplay.PAGE_SIZE);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public List<Filter> getFilters() {
		return getEntity().getFilters(null);
	}

	@Override
	public List<Order> getOrders() {
		return getEntity().getOrders(null);
	}

	@Override
	public void loadFilterOptions(final Filter filter, String condition) {
		TaskService service = this.services.getTaskService();
		FilterOptionsCallback callback = new FilterOptionsCallback() {
			@Override
			public void success(List<Filter.Option> options) {
				notifyOptions(filter, options);
			}

			@Override
			public void failure(String error) {
				notifyFilterOptionsFailure(filter);
			}
		};

		if (condition == null || condition.isEmpty())
			service.getFilterOptions(filter, callback);
		else
			service.searchFilterOptions(filter, condition, callback);
	}

	public Account getAccount() {
		return services.getAccountService().load();
	}

	public void setTaskOwner(final TaskListIndexEntry entry, final User owner) {
		TaskService service = this.services.getTaskService();
		service.saveOwner(entry.getEntity(), owner, "", new VoidCallback() {
			@Override
			public void success(Void value) {
				entry.setOwner(owner);
				notifyTaskOwner(entry);
			}

			@Override
			public void failure(String error) {
			}
		});
	}

	public void setTaskUrgent(final TaskListIndexEntry entry, final boolean value) {
		TaskService service = this.services.getTaskService();
		service.saveUrgency(entry.getEntity(), value, new VoidCallback() {
			@Override
			public void success(Void voidValue) {
				entry.setUrgent(value);
				notifyTaskUrgent(entry);
			}

			@Override
			public void failure(String error) {
			}
		});
	}

	@Override
	public int getEntriesCount() {
		if (!entriesCount.containsKey(situation))
			return -1;

		return entriesCount.get(situation);
	}

	@Override
	public void setEntriesCount(int count) {
		this.entriesCount.put(situation, count);
	}

	@Override
	protected void loadEntries(int start, int limit) {
		TaskService service = this.services.getTaskService();
		List<Filter> filters = getSelectedFilters();
		List<Order> orders = getSelectedOrders();

		TaskListIndexEntriesCallback callback = new TaskListIndexEntriesCallback() {
			@Override
			public void success(List<TaskListIndexEntry> result) {
				successCallback(result);
			}

			@Override
			public void failure(String error) {
				notifyPageFailure();
			}
		};

		String condition = getCondition();
		if (condition == null)
			service.load(situation, TaskList.Inbox.TASK_BOARD, filters, orders, start, limit, callback);
		else
			service.search(condition, situation, TaskList.Inbox.TASK_BOARD, filters, orders, start, limit, callback);
	}

	@Override
	protected void loadEntry(Entity entity) {
	}

	@Override
	protected void updateEntriesCount(int count) {
		if (entriesCount.containsKey(situation) && entriesCount.get(situation) == count)
			return;

		entriesCount.put(situation, count);
		notifyPagesCount(getPagesCount());
	}

	private void notifyTaskOwner(final TaskListIndexEntry entry) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.owner(entry);
			}
		});
	}

	private void notifyTaskUrgent(final TaskListIndexEntry entry) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.urgent(entry);
			}
		});
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public interface Hook extends IndexDisplay.Hook<TaskListIndexEntry> {
		void owner(TaskListIndexEntry entry);
		void urgent(TaskListIndexEntry entry);
	}

	public interface Page extends IndexDisplay.Page<TaskListIndexEntry> {}
	public interface Handler extends IndexDisplay.Handler<TaskListIndexDisplay, TaskListIndexEntry> {}

}
