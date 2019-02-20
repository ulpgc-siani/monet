package client.widgets.commands;

import client.presenters.operations.SearchOperation;
import client.services.TranslatorService;
import client.widgets.toolbox.SearchBoxWidget;
import com.google.gwt.user.client.ui.HTMLPanel;
import cosmos.model.ServerError;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class SearchCommand extends SearchBoxWidget {
	private final SearchOperation operation;

	public SearchCommand(SearchOperation operation, String layout, TranslatorService translator) {
		super(getHtml(layout, translator), Design.COMPACT);
		this.operation = operation;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
		this.addConditionHandler(new ConditionHandler() {
			@Override
			public void onChange(String condition) {
			}

			@Override
			public void onEnter(String condition) {
				operation.setCondition(condition);
				operation.execute();
			}
		});
	}

	private void refresh() {
		setCondition(operation.getCondition());
	}

	private void hook() {
		operation.addHook(new SearchOperation.Hook() {
			@Override
			public void show() {
			}

			@Override
			public void hide() {
			}

			@Override
			public void enable() {
			}

			@Override
			public void disable() {
			}

			@Override
			public void executePerformed() {
			}

			@Override
			public void executeFailed(ServerError details) {
			}

			@Override
			public void condition(String condition) {
				refresh();
			}
		});
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(SearchOperation.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			final String searchLayout = $(new HTMLPanel(theme.getLayout("search"))).find(toRule(StyleName.SEARCH_PANEL)).get(0).getInnerHTML();
			return new SearchCommand((SearchOperation) presenter, searchLayout, translator);
		}
	}
}
