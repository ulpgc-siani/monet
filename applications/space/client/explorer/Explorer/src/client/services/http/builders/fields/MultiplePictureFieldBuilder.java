package client.services.http.builders.fields;

import client.core.system.fields.MultipleNumberField;
import client.core.system.fields.MultiplePictureField;
import client.services.http.HttpInstance;

public class MultiplePictureFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultiplePictureField, client.core.model.fields.PictureField> {

	@Override
	public client.core.model.fields.MultiplePictureField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultiplePictureField field = new MultiplePictureField();
		initialize(field, instance);
		return field;
	}

}
