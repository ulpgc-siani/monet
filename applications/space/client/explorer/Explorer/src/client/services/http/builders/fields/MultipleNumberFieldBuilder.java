package client.services.http.builders.fields;

import client.core.system.fields.MultipleMemoField;
import client.core.system.fields.MultipleNumberField;
import client.services.http.HttpInstance;

public class MultipleNumberFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleNumberField, client.core.model.fields.NumberField> {

	@Override
	public client.core.model.fields.MultipleNumberField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleNumberField field = new MultipleNumberField();
		initialize(field, instance);
		return field;
	}

}
