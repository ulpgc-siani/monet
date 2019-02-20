package client.services;

import client.core.model.*;
import client.core.model.workmap.LineAction.Stop;
import client.core.model.workmap.WaitAction.Scale;
import client.services.callback.*;

public interface TaskService extends Service {
	void open(final String id, final TaskCallback callback);
	void open(final TaskListCallback callback);
	void saveUrgency(final Task task, final boolean value, final VoidCallback callback);
	void saveOwner(final Task task, final User owner, final String reason, final VoidCallback callback);
	void saveOwner(final Task[] tasks, final User owner, final String reason, final VoidCallback callback);
	void abort(final Task task, final VoidCallback callback);
	void loadDelegationRoles(final Task task, final RoleListCallback callback);
	void selectDelegationRole(final Task task, final Role role, final TaskCallback callback);
	void setupDelegationOrder(final Task task, final TaskCallback callback);
	void solveLine(final Task task, final Stop stop, final TaskCallback callback);
	void solveEdition(final Task task, final TaskCallback callback);
	void setupWait(Task task, Scale scale, int value, TaskCallback callback);

	void loadHistory(final Task task, int start, int limit, HistoryCallback callback);

	void load(final TaskList.Situation situation, final TaskList.Inbox inbox, final List<Filter> filters, final List<Order> orders, final int start, final int limit, final TaskListIndexEntriesCallback callback);
	void search(final String condition, final TaskList.Situation situation, final TaskList.Inbox inbox, final List<Filter> filters, final List<Order> orders, final int start, final int limit, final TaskListIndexEntriesCallback callback);
	void getFilterOptions(final Filter filter, final FilterOptionsCallback callback);
	void searchFilterOptions(final Filter filter, final String condition, final FilterOptionsCallback callback);
}
