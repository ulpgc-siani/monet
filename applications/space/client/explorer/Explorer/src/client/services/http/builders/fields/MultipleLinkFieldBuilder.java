package client.services.http.builders.fields;

import client.core.system.fields.MultipleFileField;
import client.core.system.fields.MultipleLinkField;
import client.services.http.HttpInstance;

public class MultipleLinkFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleLinkField, client.core.model.fields.LinkField> {

	@Override
	public client.core.model.fields.MultipleLinkField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleLinkField field = new MultipleLinkField();
		initialize(field, instance);
		return field;
	}

}
