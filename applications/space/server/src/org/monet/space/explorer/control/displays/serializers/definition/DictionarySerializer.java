package org.monet.space.explorer.control.displays.serializers.definition;

import com.google.gson.*;
import org.monet.metamodel.Definition;
import org.monet.space.explorer.control.displays.serializers.AbstractSerializer;
import org.monet.space.kernel.model.Dictionary;

import java.lang.reflect.Type;

public class DictionarySerializer extends AbstractSerializer<Dictionary> implements JsonSerializer<Dictionary> {

	public DictionarySerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Dictionary dictionary) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		return builder.create().toJsonTree(dictionary);
	}

	@Override
	public JsonElement serialize(Dictionary dictionary, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		JsonArray definitions = new JsonArray();

		for (Definition definition : dictionary.getAllDefinitions()) {
			JsonObject jsonDefinition = serializeDefinition(definition);
			definitions.add(jsonDefinition);
		}

		result.add("definitions", toListObject(definitions));

		return result;
	}

	private JsonObject serializeDefinition(Definition definition) {
		JsonObject result = new JsonObject();
		result.addProperty("code", definition.getCode());
		result.addProperty("name", definition.getName());
		result.addProperty("label", definition.getLabelString());
		return result;
	}

}
