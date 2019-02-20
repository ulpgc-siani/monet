package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.CatalogDefinition;
import client.services.http.HttpInstance;

public class CatalogDefinitionBuilder extends NodeDefinitionBuilder<client.core.model.definition.entity.CatalogDefinition> {

	@Override
	public client.core.model.definition.entity.CatalogDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		CatalogDefinition definition = new CatalogDefinition();
		initialize(definition, instance);
		return definition;
	}

}