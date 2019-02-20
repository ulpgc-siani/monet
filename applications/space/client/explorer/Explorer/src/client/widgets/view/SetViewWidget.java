package client.widgets.view;

import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.view.CatalogViewDisplay;
import client.presenters.displays.view.CollectionViewDisplay;
import client.presenters.displays.view.SetViewDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class SetViewWidget extends HTMLPanel {
	private final SetViewDisplay display;

	public SetViewWidget(SetViewDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		setStyleName(StyleName.VIEW);
		createComponents();
		refresh();
		hook();
	}

	private void createComponents() {
		this.createSearch();
		this.bind();
	}

	private void createSearch() {
		Element searchBox = $(getElement()).find(toRule(StyleName.SEARCH, StyleName.COMPONENT)).get(0);
		if (searchBox == null) return;

		SuggestBox search = new SuggestBox();
		this.addAndReplaceElement(search, searchBox);
	}

	private void refresh() {
	}

	private void hook() {
		this.display.addHook(new SetViewDisplay.Hook() {
			@Override
			public void loading() {
			}

			@Override
			public void update() {
				refresh();
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

	protected interface StyleName {
		String VIEW = "view";
		String SEARCH = "search";
		String COMPONENT = "component";
	}

	public static class Builder extends NodeViewWidget.Builder {
		private static final String DESIGN = "tab";
		private static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(SetViewDisplay.TYPE.toString() + DESIGN + LAYOUT, new Builder());
			registerBuilder(CollectionViewDisplay.TYPE.toString() + DESIGN + LAYOUT, new Builder());
			registerBuilder(CatalogViewDisplay.TYPE.toString() + DESIGN + LAYOUT, new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new SetViewWidget((SetViewDisplay)presenter, getLayout("view-set", layout), translator);
		}
	}
}
