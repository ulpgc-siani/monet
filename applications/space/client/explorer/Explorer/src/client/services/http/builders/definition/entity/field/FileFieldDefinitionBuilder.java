package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.FileFieldDefinition;
import client.services.http.HttpInstance;

public class FileFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.FileFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.FileFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		FileFieldDefinition definition = new FileFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

    @Override
    public void initialize(client.core.model.definition.entity.field.FileFieldDefinition object, HttpInstance instance) {
        super.initialize(object, instance);
        client.core.system.definition.entity.field.FileFieldDefinition definition = (client.core.system.definition.entity.field.FileFieldDefinition) object;
        definition.setLimit(instance.getLong("limit"));
    }
}
