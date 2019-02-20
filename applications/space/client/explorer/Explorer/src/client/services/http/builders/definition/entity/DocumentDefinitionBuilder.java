package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.DocumentDefinition;
import client.core.system.definition.entity.FormDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.field.FieldDefinitionBuilder;

public class DocumentDefinitionBuilder extends NodeDefinitionBuilder<client.core.model.definition.entity.DocumentDefinition> {

	@Override
	public client.core.model.definition.entity.DocumentDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		DocumentDefinition definition = new DocumentDefinition();
		initialize(definition, instance);
		return definition;
	}

}