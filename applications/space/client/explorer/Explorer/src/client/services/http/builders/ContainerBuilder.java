package client.services.http.builders;

import client.core.system.Container;
import client.services.http.HttpInstance;

public class ContainerBuilder extends NodeBuilder<client.core.model.Container> {

	@Override
	public client.core.model.Container build(HttpInstance instance) {
		if (instance == null)
			return null;

		Container node = new Container();
		initialize(node, instance);
		return node;
	}

	@Override
	public void initialize(client.core.model.Container object, HttpInstance instance) {
		super.initialize(object, instance);

		Container node = (Container) object;
		node.setChildren(new NodeBuilder().buildList(instance.getList("children")));
	}
}
