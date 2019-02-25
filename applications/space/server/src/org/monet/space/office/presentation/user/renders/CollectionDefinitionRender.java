package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.NodeDefinition;
import org.monet.metamodel.internal.Ref;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectionDefinitionRender extends NodeDefinitionRender {
	protected CollectionDefinition definition;

	public CollectionDefinitionRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		super.setTarget(target);
		this.definition = (CollectionDefinition) target;
	}

	protected void initCollection() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Ref> addList = collectionDefinitionAdds(this.definition);
		int pos = 0;
		String nodes = "";

		super.initCollection();

		for (Ref add : addList) {
			for (NodeDefinition definition : this.dictionary.getAllImplementersOfNodeDefinition(add.getValue())) {

				if (definition.isDisabled())
					continue;

				map.put("code", definition.getCode());
				map.put("comma", (pos != 0) ? "comma" : "");
				nodes += block("collection$node", map);
				pos++;
				map.clear();
			}
		}

		map.put("nodes", nodes);

		addMark("collection", block("collection", map));
	}

}
