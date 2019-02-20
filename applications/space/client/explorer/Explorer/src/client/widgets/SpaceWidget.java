package client.widgets;

import client.presenters.displays.SpaceDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.HTML;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;

public class SpaceWidget extends HTML {
	private final SpaceDisplay display;

	public SpaceWidget(SpaceDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	private void createComponents() {
	}

	private void refresh() {
		Element logo = $(this.getElement()).find(".logo").get(0);

		logo.setAttribute("alt", display.getName());
		logo.setAttribute("title", display.getName());

		$(logo).click(new Function() {
			@Override
			public void f() {
				display.showHome();
			}
		});
	}

	private void hook() {
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends cosmos.gwt.presenters.Presenter.Builder<TranslatorService> {

		@Override
		public boolean canBuild(Presenter presenter, String design) {
			return presenter.is(SpaceDisplay.TYPE);
		}

		@Override
		public com.google.gwt.user.client.ui.Widget build(Presenter presenter, String design, String layout) {
			return new SpaceWidget((SpaceDisplay) presenter, theme.getLayout(layout), translator);
		}
	}
}
