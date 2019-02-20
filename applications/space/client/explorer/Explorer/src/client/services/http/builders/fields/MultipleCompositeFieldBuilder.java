package client.services.http.builders.fields;

import client.core.system.fields.MultipleCompositeField;

import client.services.http.HttpInstance;

public class MultipleCompositeFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleCompositeField, client.core.model.fields.CompositeField> {

	@Override
	public client.core.model.fields.MultipleCompositeField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleCompositeField field = new MultipleCompositeField();
		initialize(field, instance);
		return field;
	}

}
