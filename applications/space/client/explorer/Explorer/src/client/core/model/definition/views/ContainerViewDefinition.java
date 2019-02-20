package client.core.model.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public interface ContainerViewDefinition extends NodeViewDefinition {
	ShowDefinition getShow();

	interface ShowDefinition {
		List<Ref> getComponent();
	}

}
