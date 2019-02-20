package client.services.http.builders.fields;

import client.services.http.builders.types.NumberBuilder;
import client.core.system.fields.NumberField;

import client.services.http.HttpInstance;

public class NumberFieldBuilder extends FieldBuilder<client.core.model.fields.NumberField> {

	@Override
	public client.core.model.fields.NumberField build(HttpInstance instance) {
		if (instance == null)
			return null;

		NumberField field = new NumberField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.NumberField object, HttpInstance instance) {
		super.initialize(object, instance);

		NumberField field = (NumberField) object;
		field.setValue(new NumberBuilder().build(instance));
	}
}
