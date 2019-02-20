package client.services.http.builders.fields;

import client.core.system.fields.SerialField;

import client.services.http.HttpInstance;

public class SerialFieldBuilder extends FieldBuilder<client.core.model.fields.SerialField> {

	@Override
	public client.core.model.fields.SerialField build(HttpInstance instance) {
		if (instance == null)
			return null;

		SerialField field = new SerialField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.SerialField object, HttpInstance instance) {
		super.initialize(object, instance);

		SerialField field = (SerialField)object;
		field.setValue(instance.getString("value"));
	}
}
