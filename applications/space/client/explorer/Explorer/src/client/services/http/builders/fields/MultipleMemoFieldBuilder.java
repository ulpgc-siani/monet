package client.services.http.builders.fields;

import client.core.system.fields.MultipleLinkField;
import client.core.system.fields.MultipleMemoField;
import client.services.http.HttpInstance;

public class MultipleMemoFieldBuilder extends MultipleFieldBuilder<client.core.model.fields.MultipleMemoField, client.core.model.fields.MemoField> {

	@Override
	public client.core.model.fields.MultipleMemoField build(HttpInstance instance) {
		if (instance == null)
			return null;

		MultipleMemoField field = new MultipleMemoField();
		initialize(field, instance);
		return field;
	}

}
