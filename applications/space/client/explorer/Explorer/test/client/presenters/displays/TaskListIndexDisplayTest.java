package client.presenters.displays;

import client.core.EntityBuilder;
import client.core.IndexBuilder;
import client.core.ListBuilder;
import client.core.TaskBuilder;
import client.core.model.*;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.core.system.MonetList;
import client.presenters.displays.view.ViewDisplay;
import client.services.*;
import client.services.callback.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class TaskListIndexDisplayTest {
	private Map<String, Integer> hookMap = new HashMap<>();

	@Test
	public void loadItemsWithStateAll() {
		TaskListIndexDisplay display = createIndexDisplay(TaskList.Situation.ALL);

		display.loadEntries(0, 10);

		assertEquals(3, (int)hookMap.get("page"));
		assertEquals(11, display.getEntriesCount());
	}

	@Test
	public void loadItemsChangeEntriesCount() {
		TaskListIndexDisplay display = createIndexDisplay(TaskList.Situation.ALL);

		assertEquals(1, display.getPagesCount());

		display.loadEntries(0, 10);
		assertEquals(3, (int)hookMap.get("page"));

		assertEquals(10, (int)hookMap.get("pagesCount"));
		assertEquals(10, display.getPagesCount());

		assertEquals(100, display.getEntriesCount());
	}

	private TaskListIndexDisplay createIndexDisplay(TaskList.Situation situation) {

		TaskListIndexDisplay display = new TaskListIndexDisplay(loadTaskList(), new TaskListIndexDisplay.Handler() {
			@Override
			public void onActivate(TaskListIndexDisplay display, TaskListIndexEntry entry) {
			}

			@Override
			public void onSelect(TaskListIndexDisplay display, TaskListIndexEntry entry) {
			}

			@Override
			public void onDelete(TaskListIndexDisplay display, TaskListIndexEntry entry) {
			}

			@Override
			public void onUnSelect(TaskListIndexDisplay display, TaskListIndexEntry entry) {
			}

			@Override
			public void onUnSelectAll(TaskListIndexDisplay display) {
			}
		}, situation);

		display.addHook(new TaskListIndexDisplay.Hook() {
			@Override
			public void owner(TaskListIndexEntry entry) {
				hookMap.put("owner", 1);
			}

			@Override
			public void urgent(TaskListIndexEntry entry) {
				hookMap.put("urgent", 1);
			}

			@Override
			public void clear() {
			}

			@Override
			public void loadingPage() {

			}

			@Override
			public void page(IndexDisplay.Page<TaskListIndexEntry> page) {
				hookMap.put("page", page.getEntries().size());
			}

			@Override
			public void pagesCount(int count) {
				hookMap.put("pagesCount", count);
			}

			@Override
			public void pageEntryAdded(TaskListIndexEntry entry) {
			}

			@Override
			public void pageEntryDeleted() {
			}

			@Override
			public void pageFailure() {

			}

			@Override
			public void pageEntryUpdated(TaskListIndexEntry entry) {

			}

			@Override
			public void entityView(ViewDisplay entityViewDisplay) {

			}

			@Override
			public void selectEntries(List<TaskListIndexEntry> entries) {

			}

			@Override
			public void selectOptions(Filter filter) {

			}

			@Override
			public void loadingOptions() {

			}

			@Override
			public void options(Filter filter, List<Filter.Option> options) {

			}
		});

		display.inject(loadServices());

		return display;
	}

	private TaskList loadTaskList() {
		return EntityBuilder.buildTaskList("", ListBuilder.EmptyTaskListViewList);
	}

	private Services loadServices() {
		return new Services() {

			@Override
			public SpaceService getSpaceService() {
				return null;
			}

			@Override
			public NodeService getNodeService() {
				return null;
			}

			@Override
			public TaskService getTaskService() {
				return new TaskService() {

					@Override
					public void open(String id, TaskCallback callback) {
					}

					@Override
					public void saveUrgency(Task task, boolean value, VoidCallback callback) {
					}

					@Override
					public void saveOwner(Task task, User owner, String reason, VoidCallback callback) {
					}

					@Override
					public void abort(Task task, VoidCallback callback) {
					}

					@Override
					public void loadDelegationRoles(Task task, RoleListCallback callback) {
					}

					@Override
					public void selectDelegationRole(Task task, Role role, TaskCallback callback) {
					}

					@Override
					public void setupDelegationOrder(Task task, TaskCallback callback) {
					}

					@Override
					public void solveLine(Task task, LineAction.Stop stop, TaskCallback callback) {
					}

					@Override
					public void solveEdition(Task task, TaskCallback callback) {
					}

					@Override
					public void setupWait(Task task, WaitAction.Scale scale, int value, TaskCallback callback) {
					}

					@Override
					public void loadHistory(Task task, int start, int limit, HistoryCallback callback) {
					}

					@Override
					public void open(TaskListCallback callback) {
						callback.success(loadTaskList());
					}

					@Override
					public void saveOwner(Task[] tasks, User owner, String reason, VoidCallback callback) {
					}

					@Override
					public void load(TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {
						MonetList<TaskListIndexEntry> entries = new MonetList<TaskListIndexEntry>() {{
							add(IndexBuilder.buildTaskListIndexEntry(TaskBuilder.buildActivity("1", "Tarea correctiva 1"), "Tarea correctiva 1"));
							add(IndexBuilder.buildTaskListIndexEntry(TaskBuilder.buildActivity("2", "Tarea correctiva 2"), "Tarea correctiva 2"));
							add(IndexBuilder.buildTaskListIndexEntry(TaskBuilder.buildActivity("3", "Tarea correctiva 3"), "Tarea correctiva 3"));
						}};
						entries.setTotalCount(100);

						callback.success(entries);
					}

					@Override
					public void search(String condition, TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {
					}

					@Override
					public void getFilterOptions(Filter filter, FilterOptionsCallback callback) {
					}

					@Override
					public void searchFilterOptions(Filter filter, String condition, FilterOptionsCallback callback) {
					}
				};
			}

			@Override
			public AccountService getAccountService() {
				return null;
			}

			@Override
			public SourceService getSourceService() {
				return null;
			}

			@Override
			public IndexService getIndexService() {
				return null;
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return null;
			}

            @Override
            public NotificationService getNotificationService() {
                return null;
            }
		};
	}

}