package client.widgets.view;

import client.core.model.Task.State;
import client.presenters.displays.view.TaskStateViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class TaskStateViewWidget extends HTMLPanel {
	private final TaskStateViewDisplay display;
	private final TranslatorService translator;

	public TaskStateViewWidget(TaskStateViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		$(this).addClass(Style.WIDGET, Style.STATE);
		bind();
	}

	private void refresh() {
		refreshTitle();
		refreshDate();
		refreshState();
		refreshLoading();
	}

	private void refreshDate() {
		Element dateBox = $(this.getElement()).find(toRule(Style.DATE)).get(0);
		dateBox.setInnerHTML(translator.translateFullDate(this.display.getDate()));
	}

	private void refreshState() {
		Element flagBox = $(this.getElement()).find(toRule(Style.FLAG)).get(0);
		State state = display.getState();

		clearStateStyles(flagBox);
		flagBox.addClassName(state.toString());
		flagBox.setInnerHTML(translator.getTaskStateLabel(state));
	}

	private void refreshLoading() {
		if (display.getState() == State.WAITING) {
			$(getElement()).find(toRule(Style.LOADING)).show();
		} else {
			$(getElement()).find(toRule(Style.LOADING)).hide();
		}
	}

	private void clearStateStyles(Element flagBox) {
		for (State state : State.values())
			flagBox.removeClassName(state.toString());
	}

	private void refreshTitle() {
		Element titleBox = $(this.getElement()).find(toRule(Style.TITLE)).get(0);
		titleBox.setInnerHTML(display.getTitle());
	}

	private void hook() {
		this.display.addHook(new TaskStateViewDisplay.Hook() {
			@Override
			public void title() {
				refreshTitle();
			}

			@Override
			public void date() {
				refreshDate();
			}

			@Override
			public void state() {
				refreshState();
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
			registerBuilder(TaskStateViewDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskStateViewWidget((TaskStateViewDisplay) presenter, getLayout("view-task-state", layout), translator);
		}
	}

	public interface Style extends TaskViewWidget.Style {
		String WIDGET = "view-task-state-widget";
		String STATE = "state";
		String DATE = "date";
		String FLAG = "flag";
		String LOADING = "loading";
		String TITLE = "title";
	}
}
