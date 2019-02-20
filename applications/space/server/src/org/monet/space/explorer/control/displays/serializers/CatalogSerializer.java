package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.CatalogDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.space.kernel.model.Node;

import java.lang.reflect.Type;

public class CatalogSerializer extends NodeSerializer<CatalogDefinition> {

	public CatalogSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(node, type, jsonSerializationContext);

		result.add("index", serializeIndex(node, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeIndex(Node node, JsonSerializationContext jsonSerializationContext) {
		CatalogDefinition definition = (CatalogDefinition)node.getDefinition();
		IndexDefinition indexDefinition = helper.getDictionary().getIndexDefinition(definition.getIndex().getValue());

		return jsonSerializationContext.serialize(indexDefinition, indexDefinition.getClass());
	}

}
