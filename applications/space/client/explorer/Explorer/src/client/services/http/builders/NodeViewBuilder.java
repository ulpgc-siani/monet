package client.services.http.builders;

import client.core.system.NodeView;
import client.services.http.HttpInstance;

public class NodeViewBuilder extends ViewBuilder<client.core.model.NodeView> {

    @Override
	public client.core.model.NodeView build(HttpInstance instance) {
		if (instance == null)
			return null;

		NodeView view = new NodeView() {
			@Override
			public ClassName getClassName() {
				return null;
			}
		};
		initialize(view, instance);
		return view;
	}
}
