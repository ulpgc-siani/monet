package client.services.http.builders.fields;

import client.services.http.builders.SourceBuilder;
import client.services.http.builders.types.CheckBuilder;
import client.core.system.fields.CheckField;

import client.services.http.HttpInstance;

public class CheckFieldBuilder extends FieldBuilder<client.core.model.fields.CheckField> {

	@Override
	public client.core.model.fields.CheckField build(HttpInstance instance) {
		if (instance == null)
			return null;

		CheckField field = new CheckField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.CheckField object, HttpInstance instance) {
		super.initialize(object, instance);

		HttpInstance value = instance.getObject("value");
		CheckField field = (CheckField) object;

		field.setValue(new CheckBuilder().buildList(value.getList("terms")));
		field.setSource(new SourceBuilder().build(value.getObject("source")));
	}
}
