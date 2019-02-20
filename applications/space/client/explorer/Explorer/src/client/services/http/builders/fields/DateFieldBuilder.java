package client.services.http.builders.fields;

import client.core.system.fields.DateField;
import client.services.http.HttpInstance;

public class DateFieldBuilder extends FieldBuilder<client.core.model.fields.DateField> {

	@Override
	public client.core.model.fields.DateField build(HttpInstance instance) {
		if (instance == null)
			return null;

		DateField field = new DateField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.DateField object, HttpInstance instance) {
		super.initialize(object, instance);
		object.setFormattedValue(instance.getString("formattedValue"));
	}
}
