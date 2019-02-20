package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.*;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.definition.RefSerializer;

import java.lang.reflect.Type;

public class CollectionDefinitionSerializer extends NodeDefinitionSerializer<CollectionDefinition> {

	public CollectionDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(CollectionDefinition definition) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Ref.class, new RefSerializer(helper));
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(CollectionDefinition definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		result.add("add", serializeAdd(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeAdd(CollectionDefinition definition, JsonSerializationContext jsonSerializationContext) {

		if (definition.getAdd() == null)
			return null;

		JsonArray node = new JsonArray();
		for (Ref addNode : definition.getAdd().getNode())
			node.add(jsonSerializationContext.serialize(addNode, addNode.getClass()));

		JsonObject addDefinition = new JsonObject();
		addDefinition.add("node", toListObject(node));

		return addDefinition;
	}

}
