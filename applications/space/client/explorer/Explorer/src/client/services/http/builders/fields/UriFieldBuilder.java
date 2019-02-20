package client.services.http.builders.fields;

import client.services.http.builders.types.UriBuilder;
import client.core.system.fields.UriField;

import client.services.http.HttpInstance;

public class UriFieldBuilder extends FieldBuilder<client.core.model.fields.UriField> {

	@Override
	public client.core.model.fields.UriField build(HttpInstance instance) {
		if (instance == null)
			return null;

		UriField field = new UriField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.UriField object, HttpInstance instance) {
		super.initialize(object, instance);

		UriField field = (UriField) object;
		field.setValue(new UriBuilder().build(instance));
	}
}
