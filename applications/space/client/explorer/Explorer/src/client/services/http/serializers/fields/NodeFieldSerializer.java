package client.services.http.serializers.fields;

import client.core.model.List;
import client.core.model.Node;
import client.core.model.definition.entity.field.NodeFieldDefinition;
import client.core.model.fields.NodeField;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class NodeFieldSerializer extends AbstractFieldSerializer<NodeField, NodeFieldDefinition, Node> {
	@Override
	protected JSONValue serializeValue(NodeField field) {
		Node node = field.getValue();

		if (node != null)
			return new JSONString(node.getId());

		return null;
	}

}
