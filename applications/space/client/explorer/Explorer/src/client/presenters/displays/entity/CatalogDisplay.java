package client.presenters.displays.entity;

import client.core.model.Catalog;
import client.core.model.CatalogView;

public class CatalogDisplay extends NodeDisplay<Catalog, CatalogView> {

	public static final Type TYPE = new Type("CatalogDisplay", NodeDisplay.TYPE);

	public CatalogDisplay(Catalog catalog, CatalogView view) {
		super(catalog, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}

}
