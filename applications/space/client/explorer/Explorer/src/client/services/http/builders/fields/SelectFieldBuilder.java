package client.services.http.builders.fields;

import client.core.system.fields.SelectField;
import client.services.http.HttpInstance;
import client.services.http.builders.SourceBuilder;
import client.services.http.builders.types.TermBuilder;

public class SelectFieldBuilder extends FieldBuilder<client.core.model.fields.SelectField> {

	@Override
	public client.core.model.fields.SelectField build(HttpInstance instance) {
		if (instance == null)
			return null;

		SelectField field = new SelectField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.SelectField object, HttpInstance instance) {
		super.initialize(object, instance);

		SelectField field = (SelectField) object;
		field.setValue(new TermBuilder().build(instance.getObject("value")));

		if (instance.getObject("value") != null)
			field.setSource(new SourceBuilder().build(instance.getObject("value").getObject("source")));
	}
}
