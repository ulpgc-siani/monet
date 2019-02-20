package client.services.http.builders;

import client.core.system.Collection;

import client.services.http.HttpInstance;

public class CollectionBuilder extends NodeBuilder<client.core.model.Collection> {

	@Override
	public client.core.model.Collection build(HttpInstance instance) {
		if (instance == null)
			return null;

		Collection node = new Collection();
		initialize(node, instance);
		return node;
	}

	@Override
	public void initialize(client.core.model.Collection object, HttpInstance instance) {
		super.initialize(object, instance);

		Collection node = (Collection)object;
		node.setIndex(new IndexBuilder().build(instance.getObject("index")));
	}

}
