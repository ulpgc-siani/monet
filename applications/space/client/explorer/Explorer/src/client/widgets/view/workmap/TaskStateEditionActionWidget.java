package client.widgets.view.workmap;

import client.presenters.displays.entity.workmap.TaskStateEditionActionDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.LoadingMessage;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

public class TaskStateEditionActionWidget extends CosmosHtmlPanel implements HoldAble {
	private final TaskStateEditionActionDisplay display;
	private LoadingMessage loadingMessage;

	public TaskStateEditionActionWidget(TaskStateEditionActionDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		createComponents(translator);
		hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		display.load();
	}

	private void createComponents(TranslatorService translator) {
		addStyleName(StyleName.WIDGET, StyleName.EDITION);
		loadingMessage = new LoadingMessage(translator);
		add(loadingMessage);
		bind();
	}

	private void showLoading() {
		loadingMessage.setVisible(true);
	}

	private void hideLoading() {
		loadingMessage.setVisible(false);
	}

	private void hook() {
		this.display.addHook(new TaskStateEditionActionDisplay.Hook() {
			@Override
			public void formLoading() {
				showLoading();
			}

			@Override
			public void formLoaded() {
				hideLoading();
			}

			@Override
			public void formFailure(String error) {
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

	public static class Builder extends TaskStateActionWidget.Builder {

		public static void register() {
			registerBuilder(TaskStateEditionActionDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskStateEditionActionWidget((TaskStateEditionActionDisplay) presenter, theme.getLayout("task-state-edition-action"), translator);
		}
	}

	public interface StyleName {
		String WIDGET = "task-state-edition-widget";
		String EDITION = "edition";
	}
}
