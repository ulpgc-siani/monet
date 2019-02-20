package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.FormDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.field.FieldDefinitionBuilder;

public class FormDefinitionBuilder extends NodeDefinitionBuilder<client.core.model.definition.entity.FormDefinition> {

	@Override
	public client.core.model.definition.entity.FormDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		FormDefinition definition = new FormDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.FormDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		FormDefinition definition = (FormDefinition) object;
		definition.setEnvironment(instance.getBoolean("environment"));
		definition.setFields(new FieldDefinitionBuilder().buildList(instance.getList("fields")));
	}

}