package client.services.http.builders.fields;

import client.core.system.fields.MemoField;

import client.services.http.HttpInstance;

public class MemoFieldBuilder extends FieldBuilder<client.core.model.fields.MemoField> {

	@Override
	public client.core.model.fields.MemoField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MemoField field = new MemoField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.MemoField object, HttpInstance instance) {
		super.initialize(object, instance);

		MemoField field = (MemoField)object;
		field.setValue(instance.getString("value"));
	}
}
