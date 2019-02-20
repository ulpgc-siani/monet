package client.services.http.builders.fields;

import client.core.system.fields.MultipleSelectField;
import client.services.http.HttpInstance;

public class MultipleSelectFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleSelectField, client.core.model.fields.SelectField> {

	@Override
	public client.core.model.fields.MultipleSelectField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleSelectField field = new MultipleSelectField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.MultipleSelectField object, HttpInstance instance) {
		super.initialize(object, instance);

		MultipleSelectField field = (MultipleSelectField)object;

		if (field.size() > 0)
	 		field.setSource(field.get(0).getSource());
	}

}
