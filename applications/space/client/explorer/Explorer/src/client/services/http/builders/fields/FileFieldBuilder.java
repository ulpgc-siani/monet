package client.services.http.builders.fields;

import client.core.system.fields.FileField;
import client.services.http.HttpInstance;
import client.services.http.builders.types.FileBuilder;

public class FileFieldBuilder extends FieldBuilder<client.core.model.fields.FileField> {

	@Override
	public client.core.model.fields.FileField build(HttpInstance instance) {
		if (instance == null)
			return null;

		FileField field = new FileField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.FileField object, HttpInstance instance) {
		super.initialize(object, instance);

		FileField field = (FileField) object;
		field.setValue(new FileBuilder().build(instance.getObject("value")));
	}
}
