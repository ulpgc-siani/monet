package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.BooleanFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.field.FieldDefinitionBuilder;

public class BooleanFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.BooleanFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.BooleanFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		BooleanFieldDefinition definition = new BooleanFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

}
