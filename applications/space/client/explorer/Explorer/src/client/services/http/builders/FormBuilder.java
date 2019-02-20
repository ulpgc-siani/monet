package client.services.http.builders;

import client.services.http.builders.fields.FieldBuilder;
import client.core.system.Form;

import client.services.http.HttpInstance;

public class FormBuilder extends NodeBuilder<client.core.model.Form> {

	@Override
	public client.core.model.Form build(HttpInstance instance) {
		if (instance == null)
			return null;

		Form node = new Form();
		initialize(node, instance);
		return node;
	}

	@Override
	public void initialize(client.core.model.Form object, HttpInstance instance) {
		super.initialize(object, instance);
		((Form) object).set(new FieldBuilder<>().buildList(instance.getList("fields")));
	}
}
