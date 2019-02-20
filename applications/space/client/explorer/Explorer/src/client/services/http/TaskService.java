package client.services.http;

import client.core.adapters.TaskDefinitionAdapter;
import client.core.adapters.TaskListAdapter;
import client.core.model.*;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.TaskDefinition;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.services.Services;
import client.services.callback.*;
import client.services.http.dialogs.task.*;

import static client.core.model.TaskList.Inbox;
import static client.core.model.TaskList.Situation;

public class TaskService extends HttpService implements client.services.TaskService {

	public TaskService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public void open(String id, final TaskCallback callback) {
		stub.request(new LoadTaskDialog(id), Task.CLASS_NAME, new TaskCallback() {
			@Override
			public void success(final Task task) {
				loadDefinition(task, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void open(TaskListCallback callback) {
		client.services.SpaceService spaceService = services.getSpaceService();
		client.services.TranslatorService translatorService = services.getTranslatorService();

		TaskList taskList = spaceService.getEntityFactory().createTaskList();
		new TaskListAdapter(translatorService).adapt(taskList);
		callback.success(taskList);
	}

	@Override
	public void saveUrgency(Task task, boolean urgent, VoidCallback callback) {
		stub.request(new SaveTaskUrgencyDialog(task, urgent), null, callback);
	}

	@Override
	public void saveOwner(Task task, User owner, String reason, VoidCallback callback) {
		stub.request(new SaveTaskOwnerDialog(task, owner, reason), null, callback);
	}

	public void saveOwner(final Task[] tasks, final User owner, final String reason, final VoidCallback callback) {
		stub.request(new SaveTasksOwnerDialog(tasks, owner, reason), null, callback);
	}

	@Override
	public void abort(Task task, VoidCallback callback) {
		stub.request(new AbortTaskDialog(task), null, callback);
	}

	@Override
	public void loadDelegationRoles(Task task, RoleListCallback callback) {
		stub.request(new LoadTaskDelegationRolesDialog(task), Role.CLASS_NAME, callback);
	}

	@Override
	public void selectDelegationRole(final Task task, Role role, final TaskCallback callback) {
		stub.request(new SelectTaskDelegationRoleDialog(task, role), Task.CLASS_NAME, new TaskCallback() {
			@Override
			public void success(Task object) {
				loadDefinition(object, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void setupDelegationOrder(final Task task, final TaskCallback callback) {
		stub.request(new SetupTaskDelegationOrderDialog(task), Task.CLASS_NAME, new TaskCallback() {
			@Override
			public void success(Task object) {
				loadDefinition(object, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void solveLine(final Task task, LineAction.Stop stop, final TaskCallback callback) {
		stub.request(new SolveTaskLineDialog(task, stop), Task.CLASS_NAME, new TaskCallback() {
			@Override
			public void success(Task object) {
				loadDefinition(object, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void solveEdition(final Task task, final TaskCallback callback) {
		stub.request(new SolveTaskEditionDialog(task), Task.CLASS_NAME, new TaskCallback() {
			@Override
			public void success(Task object) {
				loadDefinition(object, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void setupWait(final Task task, WaitAction.Scale scale, int value, final TaskCallback callback) {
		stub.request(new SetupTaskWaitDialog(task, scale, value), Task.CLASS_NAME, new TaskCallback() {
			@Override
			public void success(Task object) {
				loadDefinition(object, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void loadHistory(Task task, int start, int limit, HistoryCallback callback) {
		stub.request(new LoadTaskHistoryDialog(task, start, limit), Fact.CLASS_NAME, callback);
	}

	@Override
	public void load(Situation situation, Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, final TaskListIndexEntriesCallback callback) {
		stub.request(new LoadTaskListIndexEntriesDialog(situation, inbox, filters, orders, start, limit), TaskListIndexEntry.CLASS_NAME, callback);
	}

	@Override
	public void search(String condition, Situation situation, Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, final TaskListIndexEntriesCallback callback) {
		stub.request(new SearchTaskListIndexEntriesDialog(condition, situation, inbox, filters, orders, start, limit), TaskListIndexEntry.CLASS_NAME, callback);
	}

	@Override
	public void getFilterOptions(Filter filter, FilterOptionsCallback callback) {
		stub.request(new LoadTaskListIndexFilterOptionsDialog(filter), Filter.Option.CLASS_NAME, callback);
	}

	@Override
	public void searchFilterOptions(Filter filter, String condition, FilterOptionsCallback callback) {
		stub.request(new SearchTaskListIndexFilterOptionsDialog(filter, condition), Filter.Option.CLASS_NAME, callback);
	}

	private void loadDefinition(final Task task, final Callback callback) {
		services.getSpaceService().loadDefinition(task, new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition definition) {
				new TaskDefinitionAdapter(services.getTranslatorService()).adapt(task, (TaskDefinition) definition);
				callback.success(task);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}
}
