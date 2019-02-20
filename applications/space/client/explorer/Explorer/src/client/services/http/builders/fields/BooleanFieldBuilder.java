package client.services.http.builders.fields;

import client.core.system.fields.BooleanField;

import client.services.http.HttpInstance;

public class BooleanFieldBuilder extends FieldBuilder<client.core.model.fields.BooleanField> {

	@Override
	public client.core.model.fields.BooleanField build(HttpInstance instance) {
		if (instance == null)
			return null;

		BooleanField field = new BooleanField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.BooleanField object, HttpInstance instance) {
		super.initialize(object, instance);

		BooleanField field = (BooleanField)object;
		field.setValue(instance.getBoolean("value"));
	}
}
