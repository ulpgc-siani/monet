package client.services.http.builders.fields;

import client.core.system.fields.TextField;

import client.services.http.HttpInstance;

public class TextFieldBuilder extends FieldBuilder<client.core.model.fields.TextField> {

	@Override
	public client.core.model.fields.TextField build(HttpInstance instance) {
		if (instance == null)
			return null;

		TextField field = new TextField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.TextField object, HttpInstance instance) {
		super.initialize(object, instance);

		TextField field = (TextField)object;
		field.setValue(instance.getString("value"));
	}
}
