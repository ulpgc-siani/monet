package client.core.system;

public class CatalogView extends SetView implements client.core.model.CatalogView {

	public CatalogView() {
	}

	public CatalogView(Key key, String label, boolean isDefault, client.core.model.Node scope) {
		super(key, label, isDefault, scope);
	}
}
