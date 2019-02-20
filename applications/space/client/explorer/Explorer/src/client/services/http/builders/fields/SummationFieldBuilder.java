package client.services.http.builders.fields;

import client.core.system.fields.SummationField;

import client.services.http.HttpInstance;

public class SummationFieldBuilder extends FieldBuilder<client.core.model.fields.SummationField> {

	@Override
	public client.core.model.fields.SummationField build(HttpInstance instance) {
		if (instance == null)
			return null;

		SummationField field = new SummationField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.SummationField object, HttpInstance instance) {
		super.initialize(object, instance);

		SummationField field = (SummationField)object;
		field.setValue(instance.getString("value"));
	}
}
