package client.services.http.builders.fields;

import client.core.system.fields.CompositeField;
import client.services.http.HttpInstance;
import client.services.http.builders.types.CompositeBuilder;

public class CompositeFieldBuilder extends FieldBuilder<client.core.model.fields.CompositeField> {

	@Override
	public client.core.model.fields.CompositeField build(HttpInstance instance) {
		if (instance == null)
			return null;

		CompositeField field = new CompositeField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.CompositeField field, HttpInstance instance) {
		super.initialize(field, instance);

		if (instance.getObject("value") == null) return;
		field.setValue(new CompositeBuilder().build(instance.getObject("value")));
		field.setConditioned(instance.getObject("value").getBoolean("conditioned"));
	}

}
