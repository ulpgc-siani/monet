package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.CollectionDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Node;

import java.lang.reflect.Type;

public class CollectionSerializer extends NodeSerializer<CollectionDefinition> {

	public CollectionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(node, type, jsonSerializationContext);

		result.add("index", serializeIndex(node, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeIndex(Node node, JsonSerializationContext jsonSerializationContext) {
		CollectionDefinition definition = (CollectionDefinition)node.getDefinition();
		Ref index = definition.getIndex();

		if (index == null)
			return null;

		IndexDefinition indexDefinition = helper.getDictionary().getIndexDefinition(index.getValue());
		JsonObject result = new JsonObject();

		result.addProperty("id", indexDefinition.getCode());
		result.addProperty("entriesCount", -1);
		result.add("filters", new JsonArray());

		return result;
	}

}
