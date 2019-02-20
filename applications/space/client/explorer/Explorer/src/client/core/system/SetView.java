package client.core.system;

import client.core.model.Index;
import client.core.model.Node;
import client.core.model.Set;
import client.core.model.definition.views.SetViewDefinition;

public class SetView extends NodeView<SetViewDefinition> implements client.core.model.SetView {

	public SetView() {
	}

	public SetView(Key key, String label, boolean isDefault, Node scope) {
		super(key, label, isDefault, scope);
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.SetView.CLASS_NAME;
	}

	public Index getIndex() {
		return ((Set) getScope()).getIndex();
	}

}
