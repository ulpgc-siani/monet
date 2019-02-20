package client.services.http.builders.fields;

import client.core.system.fields.MultipleDateField;

import client.services.http.HttpInstance;

public class MultipleDateFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleDateField, client.core.model.fields.DateField> {

	@Override
	public client.core.model.fields.MultipleDateField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleDateField field = new MultipleDateField();
		initialize(field, instance);
		return field;
	}

}
