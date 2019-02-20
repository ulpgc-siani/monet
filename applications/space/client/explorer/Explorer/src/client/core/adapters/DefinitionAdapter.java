package client.core.adapters;

import client.core.model.Entity;
import client.core.model.definition.entity.EntityDefinition;

public class DefinitionAdapter {

	public void adapt(Entity entity, EntityDefinition definition) {
		entity.setDefinition(definition);
	}

}
