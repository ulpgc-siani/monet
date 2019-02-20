package client.widgets.entity;

import client.core.model.Task;
import client.presenters.displays.entity.TaskDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;

public class TaskWidget extends HTMLPanel implements HoldAble {
	private final TaskDisplay display;

    private static final String BOX = "::layout::";
	private static final String LAYOUT = "::layout::";

	public TaskWidget(TaskDisplay display, String layout, TranslatorService translator) {
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

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(TaskDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new TaskWidget((TaskDisplay) presenter, getLayout(presenter, layout), translator);
		}

		private String getLayout(Presenter presenter, String layout) {
			TaskDisplay<Task> display = (TaskDisplay) presenter;
			Task task = display.getEntity();
			String definitionClass = task.getDefinitionClass();
			String layoutName;

			layoutName = layout + " " + definitionClass;

			if (!this.theme.existsLayout(layoutName))
				layoutName = layout + " " + Task.Type.TASK.toString();

			if (!this.theme.existsLayout(layoutName))
				layoutName = layout;

			return layout.isEmpty() ? "<div class='error'>Layout not defined for task widget</div>" : this.theme.getLayout(layoutName);
		}
	}

	private void createComponents() {
		$(this.getElement()).addClass(display.getEntityType().toString(), Style.ENTITY, Style.VIEWS_COUNT + display.getViewsCount());
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
		Element labelBox = $(this.getElement()).children(".label").get(0);
		if (labelBox == null) return;
		labelBox.setInnerHTML(this.display.getLabel());
	}

	private void hook() {
		display.addHook(new TaskDisplay.Hook() {
		});
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(BOX.replaceAll(LAYOUT, layout));
	}

	private class Style {
		private static final String ENTITY = "entity task";
		private static final String VIEWS_COUNT = "vc";
	}
}
