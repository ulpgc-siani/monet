package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.ContainerDefinitionBase.ViewProperty.ShowProperty;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Attribute;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainerPageRender extends NodePageRender {

	public ContainerPageRender() {
		super();
	}

	@Override
	protected void initControlInfo() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		super.initControlInfo();

		for (Attribute attribute : this.node.getAttributeList().get().values()) {
			map.put("value", attribute.getIndicatorValue("value"));
			map.put("code", attribute.getCode());
			addMark("children", block("child", map));
			map.clear();
		}

		addMark("addList", "");
	}

	@Override
	protected void init() {
		loadCanvas("page.node.container");
		super.init();
	}

	@Override
	protected String initTab(NodeViewProperty viewDefinition) {
		String idRevision = this.getParameterAsString("idrevision");
		ContainerDefinition.ViewProperty viewContainerDefinition = (ContainerDefinition.ViewProperty) viewDefinition;

		if (!idRevision.isEmpty()) {
			ShowProperty showDefinition = viewContainerDefinition.getShow();
			ArrayList<Ref> components = showDefinition.getComponent();
			if (showDefinition != null && components != null && components.size() > 0) {
				NodeDefinition definition = this.dictionary.getNodeDefinition(components.get(0).getDefinition());
				if (definition.isCollection()) return "";
			}
		}

		return super.initTab(viewDefinition);
	}

}
