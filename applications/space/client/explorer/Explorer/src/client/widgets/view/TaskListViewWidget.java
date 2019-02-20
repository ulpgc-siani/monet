package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.view.TaskListViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class TaskListViewWidget extends HTMLPanel {
	private final TaskListViewDisplay display;

	public TaskListViewWidget(TaskListViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.bind();
	}

	private void refresh() {
		this.refreshLabel();
	}

	private void refreshLabel() {
		$(getElement()).find(toRule(StyleName.LABEL)).html(display.getLabel());
	}

	private void hook() {
		this.display.addHook(new TaskListViewDisplay.Hook() {
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends EntityViewWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(TaskListViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskListViewWidget((TaskListViewDisplay) presenter, getLayout("view-task-list", layout), translator);
		}
	}

	public interface StyleName {
		String LABEL = "label";
	}
}
