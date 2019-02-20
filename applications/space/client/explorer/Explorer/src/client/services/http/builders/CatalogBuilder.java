package client.services.http.builders;

import client.core.system.Catalog;

import client.services.http.HttpInstance;

public class CatalogBuilder extends NodeBuilder<client.core.model.Catalog> {

	@Override
	public client.core.model.Catalog build(HttpInstance instance) {
		if (instance == null)
			return null;

		Catalog node = new Catalog();
		initialize(node, instance);
		return node;
	}

	@Override
	public void initialize(client.core.model.Catalog object, HttpInstance instance) {
		super.initialize(object, instance);

		Catalog node = (Catalog)object;
		node.setIndex(new IndexBuilder().build(instance.getObject("index")));
	}

}
