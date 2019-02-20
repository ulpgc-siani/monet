package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.UriFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.field.FieldDefinitionBuilder;

public class UriFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.UriFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.UriFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		UriFieldDefinition definition = new UriFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

}
