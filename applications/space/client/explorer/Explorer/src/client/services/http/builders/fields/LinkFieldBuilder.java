package client.services.http.builders.fields;

import client.services.http.builders.IndexBuilder;
import client.services.http.builders.types.LinkBuilder;
import client.core.system.fields.LinkField;

import client.services.http.HttpInstance;

public class LinkFieldBuilder extends FieldBuilder<client.core.model.fields.LinkField> {

	@Override
	public client.core.model.fields.LinkField build(HttpInstance instance) {
		if (instance == null)
			return null;

		LinkField field = new LinkField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.LinkField object, HttpInstance instance) {
		super.initialize(object, instance);

		LinkField field = (LinkField) object;
		field.setValue(new LinkBuilder().build(instance.getObject("value")));
        if (instance.getObject("value") != null)
		    field.setIndex(new IndexBuilder().build(instance.getObject("value").getObject("index")));
	}
}
