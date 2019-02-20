package client.widgets.index.entities;

import client.core.model.definition.views.ViewDefinition;
import client.core.model.NodeIndexEntry;
import client.core.model.factory.EntityFactory;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.SetIndexDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexFacet;
import client.widgets.index.facets.FluidIndexFacet;
import client.widgets.toolbox.FluidListController;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.presenters.Presenter;

public class SetFluidIndexWidget extends SetIndexWidget implements IndexFacet.Widget {

	public SetFluidIndexWidget(SetIndexDisplay display, LayoutHelper layoutHelper, TranslatorService translator) {
		super(display, layoutHelper, translator);
		inject(new FluidIndexFacet<>(this, createDesktopHelper()));
	}

	@Override
	public void onHold(PresenterHolder holder) {
		facet.refreshListHeight();
		super.onHold(holder);
	}

	@Override
	protected void createItemsList() {
		super.createItemsList();

		new FluidListController<>(list, new FluidListController.PageHandler() {
			@Override
			public void nextPage() {
				facet.nextPage();
			}

			@Override
			public void reloadPage() {
				facet.reloadPage(false);
			}
		});
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
	public NodeIndexEntry createLoadingEntry() {
		EntityFactory entityFactory = getDisplay().getEntityFactory();
		return entityFactory.createNodeIndexEntry(entityFactory.createContainer(null), "loading");
	}

	public static class Builder extends SetIndexWidget.Builder {
		public static final String LAYOUT = ViewDefinition.Design.LIST.toString();

		public static void register() {
			registerBuilder(SetIndexDisplay.TYPE.toString() + SetIndexWidget.Builder.DESIGN + LAYOUT, new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new SetFluidIndexWidget((SetIndexDisplay)presenter, createLayoutHelper("node", layout), translator);
		}
	}

}
