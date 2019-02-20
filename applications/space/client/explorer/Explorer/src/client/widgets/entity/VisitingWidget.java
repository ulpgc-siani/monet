package client.widgets.entity;

import client.presenters.displays.VisitingDisplay;
import client.services.TranslatorService;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public abstract class VisitingWidget<T extends VisitingDisplay> extends CosmosHtmlPanel {
	protected final T display;
	protected final TranslatorService translator;

	public VisitingWidget(T display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		addStyleName(StyleName.VISITING);
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	protected abstract void createComponents();
	protected abstract void refresh();

	protected void refreshLabel() {
		$(getElement()).find(toRule(StyleName.LABEL)).first().html(display.getLabel());
	}

    protected void back() {
        display.back();
    }

	protected void hook() {
		display.addHook(new VisitingDisplay.Hook() {
			@Override
			public void label() {
				refreshLabel();
			}

			@Override
			public void labelFailure(String error) {
			}
		});
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	protected interface StyleName {
		String LABEL = "label";
		String VISITING = "visiting";
	}
}
