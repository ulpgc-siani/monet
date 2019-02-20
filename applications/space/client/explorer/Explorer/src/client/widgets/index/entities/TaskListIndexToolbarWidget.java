package client.widgets.index.entities;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.TaskListIndexEntry;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.TaskListIndexDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexToolbarWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class TaskListIndexToolbarWidget extends IndexToolbarWidget<TaskListIndexDisplay, TaskListIndexEntry> {

	public TaskListIndexToolbarWidget(TaskListIndexDisplay display, TranslatorService translator) {
		super(display, translator);
	}

	@Override
	protected void hook() {
		display.addHook(new TaskListIndexDisplay.Hook() {
			@Override
			public void clear() {

			}

			@Override
			public void loadingPage() {

			}

			@Override
			public void page(IndexDisplay.Page<TaskListIndexEntry> page) {

			}

			@Override
			public void pagesCount(int count) {

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
				refresh();
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

			@Override
			public void owner(TaskListIndexEntry entry) {

			}

			@Override
			public void urgent(TaskListIndexEntry entry) {

			}
		});
	}

	public static class Builder extends IndexToolbarWidget.Builder {

		public static void register() {
			registerBuilder(TaskListIndexDisplay.TYPE.toString() + IndexToolbarWidget.Builder.DESIGN, new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskListIndexToolbarWidget((TaskListIndexDisplay) presenter, translator);
		}
	}
}
