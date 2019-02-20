package client.services.http.builders.fields;

import client.core.system.fields.MultipleFileField;
import client.services.http.HttpInstance;

public class MultipleFileFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleFileField, client.core.model.fields.FileField> {

	@Override
	public client.core.model.fields.MultipleFileField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleFileField field = new MultipleFileField();
		initialize(field, instance);
		return field;
	}

}
