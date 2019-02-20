package client.core.system;

import client.core.model.Node;

public class CollectionView extends SetView implements client.core.model.CollectionView {

	public CollectionView() {
	}

	public CollectionView(Key key, String label, boolean isDefault, Node scope) {
		super(key, label, isDefault, scope);
	}
}
