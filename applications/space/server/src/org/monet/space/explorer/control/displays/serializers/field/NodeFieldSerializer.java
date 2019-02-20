package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.NodeFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.Node;

public class NodeFieldSerializer extends AbstractFieldSerializer<NodeFieldProperty> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		Attribute attribute = field.getAttribute();
		NodeFieldProperty fieldDefinition = field.getFieldDefinition();
		String id = getIndicatorValue(attribute, Indicator.CODE);

		if (id.isEmpty())
			return null;

		final JsonObject result = new JsonObject();
		result.add("value", serializeValueNode(fieldDefinition, attribute, jsonSerializationContext));
		return result;
	}

	private JsonElement serializeValueNode(NodeFieldProperty fieldDefinition, Attribute attribute, JsonSerializationContext jsonSerializationContext) {
		String id = getIndicatorValue(attribute, Indicator.CODE);
		Node node = helper.loadNode(id);
		return jsonSerializationContext.serialize(node, node.getClass());
	}

}
