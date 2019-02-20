package client.core.system;

import client.core.model.definition.views.DocumentViewDefinition;

public class DocumentView extends NodeView<DocumentViewDefinition> implements client.core.model.DocumentView {

	public DocumentView() {
	}

	public DocumentView(client.core.model.Key key, String label, boolean isDefault, client.core.model.Node scope) {
		super(key, label, isDefault, scope);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.DocumentView.CLASS_NAME;
	}

}
