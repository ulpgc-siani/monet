package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.displays.view.CatalogViewDisplay;
import client.presenters.displays.view.CollectionViewDisplay;
import client.presenters.displays.view.SetViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class SetViewDocumentWidget extends CosmosHtmlPanel implements HoldAble {
	private final SetViewDisplay display;
	private final Section.Builder sectionBuilder;
	private final TranslatorService translator;
	private Label label;
	private VerticalPanel sections;
	private HTML loading;

	public SetViewDocumentWidget(SetViewDisplay display, String layout, TranslatorService translator, Section.Builder sectionBuilder) {
		super(getHtml(layout, translator));
		addStyleName(StyleName.VIEW, StyleName.DOCUMENT);
		this.display = display;
		this.translator = translator;
		this.sectionBuilder = sectionBuilder;
		createComponents();
		hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		refresh();
	}

	private void createComponents() {
		createLabel();
		createLoading();
		createSections();
		bind();
	}

	private void createLabel() {
		label = new Label();

		if (display.getLabel() != null)
			label.setText(display.getLabel());
		else
			label.setVisible(false);
	}

	private void createLoading() {
		loading = new HTML(translator.translate(TranslatorService.Label.LOADING));
		loading.addStyleName(StyleName.LOADING);
		loading.setVisible(true);
	}

	private void createSections() {
		sections = new VerticalPanel();
	}

	private void refresh() {
		refreshSections();
	}

	private void refreshSections() {
		sections.clear();

		for (SetIndexDisplay setIndexDisplay : display.getIndexes())
			sections.add(sectionBuilder.build(setIndexDisplay));
	}

	private void showLoading() {
		loading.setVisible(true);
	}

	private void hideLoading() {
		loading.setVisible(false);
	}

	private void hook() {
		display.addHook(new SetViewDisplay.Hook() {
			@Override
			public void loading() {
				showLoading();
			}

			@Override
			public void update() {
				refresh();
				hideLoading();
			}
		});
	}

	private void bind() {
		bindKeepingStyles(label, toRule(StyleName.LABEL));
		bindKeepingStyles(loading, toRule(StyleName.LOADING));
        bindKeepingStyles(sections, toRule(StyleName.SECTIONS));

        this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	private interface StyleName extends SetViewWidget.StyleName {
		String DOCUMENT = "document";
		String LABEL = "label";
		String LOADING = "loading";
		String SECTIONS = "sections";
	}

	public static class Builder extends SetViewWidget.Builder {
		private static final String DESIGN = "tab";
		private static final String LAYOUT = ViewDefinition.Design.DOCUMENT.toString();

		public static void register() {
			registerBuilder(SetViewDisplay.TYPE.toString() + DESIGN + LAYOUT, new Builder());
			registerBuilder(CollectionViewDisplay.TYPE.toString() + DESIGN + LAYOUT, new Builder());
			registerBuilder(CatalogViewDisplay.TYPE.toString() + DESIGN + LAYOUT, new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new SetViewDocumentWidget((SetViewDisplay)presenter, getLayout("view-set", "document"), translator, setUpBuilder(new Section.Builder()));
		}
	}

}
