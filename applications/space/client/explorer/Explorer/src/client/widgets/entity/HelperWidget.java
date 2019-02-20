package client.widgets.entity;

import client.core.model.HtmlPage;
import client.presenters.displays.HelperDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.HTMLPanel;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class HelperWidget extends HTMLPanel {
	private final HelperDisplay display;

	public HelperWidget(HelperDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		addStyleName(StyleName.HELPER);
		this.display = display;
		refresh();
		hook();
	}

	private void refresh() {
		setVisible(display.isVisible());
		refreshPage();
	}

	private void refreshPage() {
		HtmlPage page = display.getPage();
		if (page != null)
		    $(getElement()).find(toRule(StyleName.PAGE)).html(page.getContent());
    }

	private void hook() {
		display.addHook(new HelperDisplay.Hook() {
			@Override
			public void page() {
				refresh();
			}

			@Override
			public void pageError(String error) {
			}
		});
	}

	private static String getHtml(String layout, TranslatorService translator) {
		return translator.translateHTML(layout);
	}

	private interface StyleName {
		String HELPER = "helper";
		String PAGE = "page";
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(HelperDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new HelperWidget((HelperDisplay) presenter, theme.getLayout("helper"), translator);
		}
	}
}
