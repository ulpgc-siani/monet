package client.core.adapters;

import client.core.model.Form;
import client.core.model.Node;
import client.core.model.NodeView;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.entity.NodeDefinition;
import client.core.system.ViewList;
import client.services.TranslatorService;

public class NodeDefinitionAdapter {
	private TranslatorService translatorService;

	public NodeDefinitionAdapter(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

	public void adapt(Node node, NodeDefinition definition) {
		node.setDefinition(definition);

		adaptViews(node);

		if (node.isForm())
			new FormDefinitionAdapter(translatorService).adapt((Form)node, (FormDefinition)definition);
	}

	private void adaptViews(client.core.model.Node node) {
		for (NodeView view : (ViewList<NodeView>)node.getViews())
			if (view.equals(NodeView.PREVIEW))
				view.setLabel(translate(TranslatorService.Label.PREVIEW));
	}

	private String translate(TranslatorService.Label label) {
		return translatorService.translate(label);
	}

}
