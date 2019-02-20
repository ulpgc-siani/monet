package client.services.http.builders;

import client.core.system.Desktop;

import client.services.http.HttpInstance;

public class DesktopBuilder extends NodeBuilder<client.core.model.Desktop> {

	@Override
	public client.core.model.Desktop build(HttpInstance instance) {
		if (instance == null)
			return null;

		Desktop node = new Desktop();
		initialize(node, instance);
		return node;
	}

	@Override
	public void initialize(client.core.model.Desktop object, HttpInstance instance) {
		super.initialize(object, instance);

		Desktop node = (Desktop)object;
		node.setSingletons(new NodeBuilder<>().buildList(instance.getList("singletons")));
	}
}
