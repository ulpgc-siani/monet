package client.core.model.definition.entity;

import client.core.model.definition.Ref;
import client.core.model.List;

public interface CollectionDefinition extends SetDefinition {
	AddDefinition getAdd();

	interface AddDefinition {
		List<Ref> getNode();
	}
}
