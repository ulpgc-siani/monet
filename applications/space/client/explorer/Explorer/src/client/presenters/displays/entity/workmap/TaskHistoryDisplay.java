package client.presenters.displays.entity.workmap;

import client.core.model.Fact;
import client.core.model.List;
import client.core.model.MonetLink;
import client.core.model.Task;
import client.presenters.displays.Display;
import client.presenters.operations.ExecuteMonetLinkOperation;
import client.services.callback.HistoryCallback;

public class TaskHistoryDisplay extends Display {
	private int currentPage = 0;
	private int pageSize = 10;
	private int entriesCount = -1;
	private Task task;

	public static final Type TYPE = new Type("TaskHistoryDisplay", Display.TYPE);

	public TaskHistoryDisplay(Task task) {
        this.task = task;
	}

	@Override
	protected void onInjectServices() {
	}

	public Task getTask() {
		return task;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public void firstPage() {
		notifyClear();
		loadPage(0);
	}

	public void nextPage() {
		loadPage(currentPage + 1);
	}

	private void loadPage(int page) {

		if (!check(page))
			return;

		currentPage = page;

		services.getTaskService().loadHistory(task, getStart(), getLimit(), new HistoryCallback() {
			@Override
			public void success(final List<Fact> object) {
				entriesCount = object.getTotalCount();
				notifyPage(new Page() {
					@Override
					public int getId() {
						return currentPage;
					}

					@Override
					public List<Fact> getEntries() {
						return object;
					}
				});
			}

			@Override
			public void failure(String error) {
				notifyPageFailure(error);
			}
		});
	}

	public boolean hasMorePages() {
		return currentPage == getPagesCount()-1;
	}

	public void executeLink(MonetLink link) {
		ExecuteMonetLinkOperation operation = new ExecuteMonetLinkOperation(getOperationContext(), link);
		operation.inject(services);
		operation.execute();
	}

	public interface Hook extends Display.Hook {
		void clearPages();
		void page(Page page);
		void pageFailure(String error);
	}

	public interface Page {
		int getId();
		List<Fact> getEntries();
	}

	private int getStart() {
		return currentPage*pageSize;
	}

	private boolean check(int page) {
		return page >= 0 && page < getPagesCount();
	}

	private int getPagesCount() {
		if (entriesCount == -1)
			return 1;

		return Math.round(entriesCount / pageSize) + ((entriesCount % pageSize) > 0?1:0);
	}

	private int getLimit() {
		return pageSize;
	}

	private void notifyClear() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.clearPages();
			}
		});
	}

	private void notifyPage(final Page page) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.page(page);
			}
		});
	}

	private void notifyPageFailure(final String error) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageFailure(error);
			}
		});
	}
}
