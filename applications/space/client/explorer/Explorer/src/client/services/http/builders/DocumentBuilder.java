package client.services.http.builders;

import client.core.system.Container;
import client.core.system.Document;
import client.services.http.HttpInstance;

public class DocumentBuilder extends NodeBuilder<client.core.model.Document> {

	@Override
	public client.core.model.Document build(HttpInstance instance) {
		if (instance == null)
			return null;

		Document node = new Document();
		initialize(node, instance);
		return node;
	}

}
