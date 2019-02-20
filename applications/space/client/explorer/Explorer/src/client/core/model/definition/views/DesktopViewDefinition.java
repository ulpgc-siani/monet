package client.core.model.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public interface DesktopViewDefinition extends NodeViewDefinition {
	ShowDefinition getShow();

	interface ShowDefinition {
		List<Ref> getLinks();
	}

}
