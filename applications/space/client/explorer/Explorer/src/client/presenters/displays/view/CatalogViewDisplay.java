package client.presenters.displays.view;

import client.core.model.Catalog;
import client.core.model.CatalogView;
import client.core.model.List;
import client.core.model.Node;
import client.core.system.MonetList;

public class CatalogViewDisplay extends SetViewDisplay {

	public static final Type TYPE = new Type("CatalogViewDisplay", SetViewDisplay.TYPE);

	public CatalogViewDisplay(Node node, CatalogView nodeView) {
        super(node, nodeView);
	}

	@Override
	protected void onInjectServices() {
		super.onInjectServices();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	protected List<String> getEntityAddList() {
		return new MonetList<>();
	}

	public static class Builder extends ViewDisplay.Builder<Node, CatalogView> {

		protected static void register() {
			registerBuilder(Catalog.CLASS_NAME.toString() + CatalogView.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public ViewDisplay build(Node entity, CatalogView view) {
			return new CatalogViewDisplay(entity, view);
		}
	}

}
