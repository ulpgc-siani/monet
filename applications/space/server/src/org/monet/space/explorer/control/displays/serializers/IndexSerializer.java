package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.metamodel.IndexDefinition;

import java.lang.reflect.Type;

public class IndexSerializer extends AbstractSerializer<IndexDefinition> implements JsonSerializer<IndexDefinition> {

	public IndexSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(IndexDefinition definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(IndexDefinition definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("id", definition.getCode());

		return result;
	}

}
