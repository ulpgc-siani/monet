package client.services.http.builders.fields;

import client.core.system.fields.MultipleTextField;

import client.services.http.HttpInstance;

public class MultipleTextFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleTextField, client.core.model.fields.TextField> {

	@Override
	 public client.core.model.fields.MultipleTextField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleTextField field = new MultipleTextField();
		initialize(field, instance);
		return field;
	}

}
