package client.widgets.entity;

import client.core.model.TaskList;
import client.presenters.displays.entity.TaskListDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;

public class TaskListWidget extends HTMLPanel implements HoldAble {
	private final TaskListDisplay display;

	public TaskListWidget(TaskListDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;

		this.createComponents();
		this.refresh();
		this.hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		setHeight("90%");
	}

	private void createComponents() {
		$(this.getElement()).addClass(StyleName.Entity);
		this.bind();
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private void refresh() {
		this.refreshLabel();
	}

	private void refreshLabel() {
		Element labelBox = $(this.getElement()).find(".label").get(0);
		if (labelBox == null) return;
		labelBox.setInnerHTML(this.display.getLabel());
	}

	private void hook() {
		display.addHook(new TaskListDisplay.Hook() {
		});
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(TaskListDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new TaskListWidget((TaskListDisplay) presenter, getLayout(presenter, layout), translator);
		}

		private String getLayout(Presenter presenter, String layout) {
			TaskListDisplay display = (TaskListDisplay) presenter;
			TaskList taskList = display.getEntity();
			String definitionClass = taskList.getDefinitionClass();
			String layoutName = layout + " " + definitionClass;

			if (!this.theme.existsLayout(layoutName))
				layoutName = layout;

			return layout.isEmpty() ? "<div class='error'>Layout not defined for node widget</div>" : this.theme.getLayout(layoutName);
		}

	}

	private class StyleName {
		private static final String Entity = "entity task-list";
	}
}
