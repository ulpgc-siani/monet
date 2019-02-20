package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.*;
import org.monet.metamodel.NodeFieldProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class NodeFieldDefinitionSerializer extends FieldDefinitionSerializer<NodeFieldProperty> {

	public NodeFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(NodeFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("contain", serializeContain(definition, jsonSerializationContext));
		result.add("add", serializeAdd(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeContain(NodeFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getContain() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("node", helper.getDictionary().getDefinitionCode(definition.getContain().getNode().getValue()));

		return result;
	}

	private JsonElement serializeAdd(NodeFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getAdd() == null)
			return null;

		JsonArray nodes = new JsonArray();
		for (Ref node : definition.getAdd().getNode()) {
			nodes.add(new JsonPrimitive(helper.getDictionary().getDefinitionCode(node.getValue())));
		}

		JsonObject result = new JsonObject();
		result.add("nodes", nodes);

		return result;
	}

}