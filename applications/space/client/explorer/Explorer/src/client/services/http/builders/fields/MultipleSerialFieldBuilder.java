package client.services.http.builders.fields;

import client.core.system.fields.MultiplePictureField;
import client.core.system.fields.MultipleSerialField;
import client.services.http.HttpInstance;

public class MultipleSerialFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleSerialField, client.core.model.fields.SerialField> {

	@Override
	public client.core.model.fields.MultipleSerialField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleSerialField field = new MultipleSerialField();
		initialize(field, instance);
		return field;
	}

}
