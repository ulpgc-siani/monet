package client.widgets.index.entities;

import client.core.model.definition.views.ViewDefinition;
import client.core.model.IndexEntry;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexFacet;
import client.widgets.index.facets.SolidIndexFacet;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;


public class SetSolidIndexWidget extends SetIndexWidget implements IndexFacet.Widget {

	public SetSolidIndexWidget(SetIndexDisplay display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(display, layoutHelper, translator);
		inject(new SolidIndexFacet<>(this));
	}

	@Override
	public HTMLListWidget getList() {
		return list;
	}

	@Override
	public IndexDisplay getDisplay() {
		return display;
	}

	@Override
	public IndexEntry createLoadingEntry() {
		return null;
	}

	public static class Builder extends SetIndexWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.DOCUMENT.toString();

		public static void register() {
			registerBuilder(SetIndexDisplay.TYPE.toString() + SetIndexWidget.Builder.DESIGN + LAYOUT, new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new SetSolidIndexWidget((SetIndexDisplay)presenter, createLayoutHelper("node", layout), translator);
		}

	}
}
