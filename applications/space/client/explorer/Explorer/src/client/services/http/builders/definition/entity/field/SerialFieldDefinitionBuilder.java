package client.services.http.builders.definition.entity.field;

import client.core.system.definition.entity.field.SerialFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.field.FieldDefinitionBuilder;

public class SerialFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.SerialFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.SerialFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		SerialFieldDefinition definition = new SerialFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.SerialFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		SerialFieldDefinition definition = (SerialFieldDefinition)object;
		definition.setSerial(getSerial(instance.getObject("serial")));
	}

	private client.core.model.definition.entity.field.SerialFieldDefinition.SerialDefinition getSerial(HttpInstance instance) {
		if (instance == null)
			return null;

		SerialFieldDefinition.SerialDefinition serial = new SerialFieldDefinition.SerialDefinition();
		serial.setName(instance.getString("name"));
		serial.setFormat(instance.getString("format"));

		return serial;
	}

}
