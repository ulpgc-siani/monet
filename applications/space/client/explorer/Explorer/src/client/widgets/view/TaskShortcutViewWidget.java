package client.widgets.view;

import client.presenters.displays.view.TaskShortcutViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;

public class TaskShortcutViewWidget extends HTMLPanel {
	private final TaskShortcutViewDisplay display;
	private final TranslatorService translator;

	public TaskShortcutViewWidget(TaskShortcutViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		$(this).addClass(Style.WIDGET, Style.SHORTCUT);
		bind();
	}

	private void refresh() {
		clear();

		if (display.getShortcut() != null)
			add(Builder.getViewBuilder().build(display.getShortcut(), "tab", "_defined-by-view-definition_"));
	}

	private void hook() {
		this.display.addHook(new TaskShortcutViewDisplay.Hook() {
			@Override
			public void shortcut() {
				refresh();
			}

			@Override
			public void shortcutFailure(String error) {
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends TaskViewWidget.Builder {

		public static void register() {
			registerBuilder(TaskShortcutViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskShortcutViewWidget((TaskShortcutViewDisplay) presenter, getLayout("view-task-shortcut", layout), translator);
		}
	}

	public interface Style extends TaskViewWidget.Style {
		String WIDGET = "view-task-shortcut-widget";
		String SHORTCUT = "shortcut";
	}
}
