package client.widgets;

import client.core.model.HtmlPage;
import client.presenters.displays.ApplicationDisplay;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import cosmos.presenters.Presenter;
import cosmos.services.TranslatorService;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class ApplicationWidget extends HTMLPanel {

	private final ApplicationDisplay display;

	public ApplicationWidget(ApplicationDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		addStyleName(Style.ENVIRONMENT);
		if (display.getEnvironmentCode() != null) addStyleName(display.getEnvironmentCode());
		this.display = display;
		hook();
		refresh();
	}

	public void refresh() {
		Document.get().setTitle(display.getTitle());
		refreshHelper();
	}

	private void refreshHelper() {
		Element helperDisplayBox = $(this).find(toRule(Style.HELPER_DISPLAY)).get(0);

		if (helperDisplayBox == null)
			return;

		if (display.isHelperVisible())
            helperDisplayBox.addClassName(Style.VISIBLE);
		else
            helperDisplayBox.removeClassName(Style.VISIBLE);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(ApplicationDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new ApplicationWidget((ApplicationDisplay) presenter, theme.getLayout(layout), translator);
		}
	}

	private void hook() {
		display.addHook(new ApplicationDisplay.Hook() {
			@Override
			public void helperPage(HtmlPage page) {
				refresh();
			}
		});
	}

	private interface Style {
		String ENVIRONMENT = "environment";
		String HELPER_DISPLAY = "helper-display";
		String VISIBLE = "visible";
	}
}
