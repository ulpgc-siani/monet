package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.internal.Ref;

import java.util.ArrayList;
import java.util.HashMap;

public class ContainerDefinitionRender extends NodeDefinitionRender {
	protected ContainerDefinition definition;

	public ContainerDefinitionRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		super.setTarget(target);
		this.definition = (ContainerDefinition) target;
	}

	protected void initCollection() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Ref> containList = this.definition.getContain().getNode();
		int pos = 0;
		String nodes = "";

		super.initCollection();

		for (Ref contain : containList) {
			NodeDefinition definition = this.dictionary.getNodeDefinition(contain.getValue());
			if (definition.isDisabled()) continue;
			map.put("code", definition.getCode());
			map.put("comma", (pos != 0) ? "comma" : "");
			nodes += block("collection$node", map);
			pos++;
			map.clear();
		}

		map.put("nodes", nodes);

		addMark("collection", block("collection", map));
	}

}
