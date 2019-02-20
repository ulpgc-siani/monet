package client.core.system;

import client.core.model.Node;

public class ProxyCollectionView extends CollectionView implements client.core.model.ProxyCollectionView {
	public ProxyCollectionView(Key key, String label, boolean isDefault, Node scope) {
		super(key, label, isDefault, scope);
	}
}
