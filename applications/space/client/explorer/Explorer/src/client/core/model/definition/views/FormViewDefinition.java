package client.core.model.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public interface FormViewDefinition extends NodeViewDefinition {
	ShowDefinition getShow();

	interface ShowDefinition {
		String getLayout();
		String getLayoutExtended();
		List<Ref> getFields();
	}

}
