package client.widgets.entity.visiting;

import client.presenters.displays.SearchVisitingDisplay;
import client.services.TranslatorService;
import client.widgets.entity.VisitingWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class SearchVisitingWidget extends VisitingWidget<SearchVisitingDisplay> {
	private Anchor backButton;

	public SearchVisitingWidget(SearchVisitingDisplay display, String layout, TranslatorService translator) {
		super(display, layout, translator);
	}

	@Override
	protected void createComponents() {
		createBackButton();
		this.bind();
	}

	private void createBackButton() {
		backButton = new Anchor(translator.translate(TranslatorService.Label.BACK).toLowerCase());
		backButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				back();
			}
		});
	}

	private void bind() {
		bindKeepingStyles(backButton, toRule(StyleName.BACK));

		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	@Override
	protected void refresh() {
		refreshLabel();
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(SearchVisitingDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new SearchVisitingWidget((SearchVisitingDisplay) presenter, getLayout("search-results"), translator);
		}

		private String getLayout(String layout) {
			String content = theme.getLayout(layout);
			return content.isEmpty() ? "<div class='error'>Layout not defined for search results visiting widget</div>" : content;
		}
	}

	private interface StyleName extends VisitingWidget.StyleName {
		String BACK = "back";
	}

}
