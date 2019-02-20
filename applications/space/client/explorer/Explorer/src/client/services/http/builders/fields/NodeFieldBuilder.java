package client.services.http.builders.fields;

import client.core.model.Node;
import client.core.system.fields.NodeField;
import client.services.http.HttpInstance;
import client.services.http.builders.NodeBuilder;

public class NodeFieldBuilder extends FieldBuilder<client.core.model.fields.NodeField> {

	@Override
	public client.core.model.fields.NodeField build(HttpInstance instance) {
		if (instance == null)
			return null;

		NodeField field = new NodeField();
		initialize(field, instance);
		return field;
	}

	@Override
	public void initialize(client.core.model.fields.NodeField object, HttpInstance instance) {
		super.initialize(object, instance);

		NodeField field = (NodeField) object;
		field.setValue((Node)new NodeBuilder().build(instance.getObject("value")));
	}
}
