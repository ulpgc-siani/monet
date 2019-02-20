package org.monet.space.explorer.control.dialogs;

import com.google.inject.Inject;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

import java.util.ArrayList;
import java.util.List;

public class DeleteNodesDialog extends HttpDialog {
	private LayerProvider layerProvider;

	@Inject
	public void inject(LayerProvider layerProvider) {
		this.layerProvider = layerProvider;
	}

	public Node[] getNodes() {
		NodeLayer nodeLayer = layerProvider.getNodeLayer();
		String[] ids = getEntityIds();
		List<Node> result = new ArrayList<>();

		for (String id : ids)
			result.add(nodeLayer.loadNode(id));

		return result.toArray(new Node[result.size()]);
	}

}
