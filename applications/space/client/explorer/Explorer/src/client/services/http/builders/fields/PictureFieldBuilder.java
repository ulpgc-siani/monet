package client.services.http.builders.fields;

import client.services.http.builders.types.PictureBuilder;
import client.core.system.fields.PictureField;

import client.services.http.HttpInstance;

public class PictureFieldBuilder extends FieldBuilder<client.core.model.fields.PictureField> {

	@Override
	public client.core.model.fields.PictureField build(HttpInstance instance) {
		if (instance == null)
			return null;

		PictureField field = new PictureField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.PictureField object, HttpInstance instance) {
		super.initialize(object, instance);

		PictureField field = (PictureField) object;
		field.setValue(new PictureBuilder().build(instance.getObject("value")));
	}
}
